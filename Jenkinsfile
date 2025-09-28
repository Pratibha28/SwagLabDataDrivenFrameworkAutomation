pipeline {
  agent any

  tools {
    maven 'Maven 3.9.9'
    jdk 'JDK_17'
  }

  parameters {
    booleanParam(name: 'START_GRID', defaultValue: true, description: 'Try to start grid if not already running')
    string(name: 'COMPOSE_PATH', defaultValue: 'selenium-grid\\docker-compose.yml', description: 'compose file path')
    string(name: 'SELENIUM_GRID_URL', defaultValue: 'http://localhost:4444/wd/hub', description: 'Grid URL to use for tests')
  }

  environment {
    SKIP_START_GRID = 'false'
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

    stage('Detect existing Grid') {
      steps {
        script {
          // Check for running containers whose name contains "selenium" or "selenium-hub"
          def checkCmd = 'docker ps --format "{{.Names}} {{.Image}}"'
          def out = bat(script: checkCmd, returnStdout: true).trim()
          echo "docker ps output:\n${out}"

          if (out.toLowerCase().contains('selenium') || out.toLowerCase().contains('selenium-hub') || out.toLowerCase().contains('selenium-standalone')) {
            env.SKIP_START_GRID = 'true'
            echo "Detected existing Selenium container. Will skip starting Grid in this job."
          } else {
            env.SKIP_START_GRID = 'false'
            echo "No existing Selenium container detected. Will start Grid if START_GRID=true."
          }
        }
      }
    }

    stage('Start Grid (conditional)') {
      when { expression { return params.START_GRID && env.SKIP_START_GRID == 'false' } }
      steps {
        echo "Starting Selenium Grid using compose file: ${params.COMPOSE_PATH}"
        // ensure compose file exists
        bat """
          if not exist "${params.COMPOSE_PATH}" (
            echo ERROR: compose file not found at ${params.COMPOSE_PATH}
            exit /b 1
          )
        """

        // Start compose (try docker compose then docker-compose)
        bat '''
          docker compose version >nul 2>&1
          IF %ERRORLEVEL% EQU 0 (
             docker compose -f %COMPOSE_PATH% up -d
          ) ELSE (
             docker-compose -f %COMPOSE_PATH% up -d
          )
        '''
        // wait a bit
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
        // Only attempt to shut down compose if we actually started it in this job
        if (params.START_GRID && env.SKIP_START_GRID == 'false') {
          echo "Stopping Selenium Grid (compose down)"
          bat '''
            docker compose version >nul 2>&1
            IF %ERRORLEVEL% EQU 0 (
              docker compose -f %COMPOSE_PATH% down --volumes --remove-orphans
            ) ELSE (
              docker-compose -f %COMPOSE_PATH% down --volumes --remove-orphans
            )
          '''
        } else {
          echo "Skipping compose down (we didn't start the Grid in this job)."
        }
      }
    }
    failure {
      echo "Build failed - check console for details"
    }
  }
}
