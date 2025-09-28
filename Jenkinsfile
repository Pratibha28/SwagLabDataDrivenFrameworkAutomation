pipeline {
  agent any

  tools {
    maven 'Maven 3.9.9'
    jdk 'JDK_17'
  }

  parameters {
    booleanParam(name: 'START_GRID', defaultValue: true, description: 'Start grid via docker-compose')
    string(name: 'COMPOSE_PATH', defaultValue: 'selenium-grid\\docker-compose.yml', description: 'Path to docker-compose file')
    string(name: 'SELENIUM_GRID_URL', defaultValue: 'http://localhost:4444/wd/hub', description: 'Grid URL for tests')
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'SwagLabThread', url: 'https://github.com/Pratibha28/SwagLabDataDrivenFrameworkAutomation.git'
      }
    }

    stage('Build') {
      steps {
        bat 'mvn clean compile'
      }
    }

    stage('Stop existing Grid (safe)') {
      when { expression { return params.START_GRID } }
      steps {
        echo "🛑 Safely stopping any existing Selenium containers (no-op if none)..."
        // This block is written carefully so it never returns a non-zero exit code when there are no containers
        bat '''
          REM list container ids whose name contains "selenium" into running.txt (may be empty)
          docker ps -q --filter "name=selenium" > running.txt 2>nul || echo. > running.txt

          REM check if running.txt has any content
          set FILESIZE=0
          for %%I in (running.txt) do set FILESIZE=%%~zI

          if %FILESIZE% EQU 0 (
            echo No selenium containers running. Nothing to stop.
          ) else (
            echo Found selenium container ids:
            type running.txt
            for /F "usebackq tokens=*" %%c in ("running.txt") do (
              echo Attempting to stop container %%c ...
              docker stop %%c || echo "Warning: docker stop failed for %%c (it may have already exited)"
              docker rm %%c   >nul 2>&1 || echo "Note: docker rm returned non-zero for %%c (might be already removed)"
            )
          )

          del running.txt 2>nul || echo.
        '''
      }
    }

    stage('Start Grid') {
      when { expression { return params.START_GRID } }
      steps {
        echo "🚀 Starting Selenium Grid using compose file: ${params.COMPOSE_PATH}"
        bat """
          if not exist "${params.COMPOSE_PATH}" (
            echo ERROR: Compose file not found at ${params.COMPOSE_PATH}
            exit /b 1
          )
        """
        bat """
          docker compose version >nul 2>&1
          IF %ERRORLEVEL% EQU 0 (
             docker compose -f ${params.COMPOSE_PATH} up -d
          ) ELSE (
             docker-compose -f ${params.COMPOSE_PATH} up -d
          )
        """
        bat 'powershell -Command "Start-Sleep -Seconds 8"'
      }
    }

    stage('Test') {
      steps {
        bat """
          mvn test ^
            -Dsurefire.suiteXmlFiles=testngaddressforextentreport.xml ^
            -Denv=qa ^
            -Dbrowser=chrome ^
            -DrunOnGrid=true ^
            -DseleniumGridUrl=${params.SELENIUM_GRID_URL} ^
            -Dheadless=true
        """
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/ExtentReports.html, target/screenshots/*.png', allowEmptyArchive: true

      script {
        if (params.START_GRID) {
          echo "🛑 Bringing down Grid if we started it (best-effort)"
          bat """
            docker compose version >nul 2>&1
            IF %ERRORLEVEL% EQU 0 (
              docker compose -f ${params.COMPOSE_PATH} down --volumes --remove-orphans
            ) ELSE (
              docker-compose -f ${params.COMPOSE_PATH} down --volumes --remove-orphans
            )
          """
        } else {
          echo "START_GRID=false → skipping Grid shutdown"
        }
      }
    }
    failure {
      echo "❌ Build failed - check console output above for details"
    }
  }
}
