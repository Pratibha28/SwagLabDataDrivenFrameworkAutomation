pipeline {
  agent any

  tools {
    maven 'Maven 3.9.9'
    jdk 'JDK_17'
  }

  parameters {
    booleanParam(name: 'START_GRID', defaultValue: true, description: 'Try to start grid if not already running')
    string(name: 'COMPOSE_PATH', defaultValue: 'selenium-grid\\docker-compose.yml', description: 'Path to docker-compose file')
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
          def out = bat(script: 'docker ps --format "{{.Names}} {{.Image}}"', returnStdout: true).trim()
          echo "docker ps output:\n${out}"

          if (out.toLowerCase().contains('selenium')) {
            env.SKIP_START_GRID = 'true'
            echo "✅ Existing Selenium container detected → skipping Grid startup"
          } else {
            env.SKIP_START_GRID = 'false'
            echo "ℹ️ No existing Selenium container detected → will start Grid if START_GRID=true"
          }
        }
      }
    }

    stage('Start Grid (conditional)') {
      when { expression { return params.START_GRID && env.SKIP_START_GRID == 'false' } }
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
        if (params.START_GRID && env.SKIP_START_GRID == 'false') {
          echo "🛑 Stopping Selenium Grid (compose down)"
          bat """
            docker compose version >nul 2>&1
            IF %ERRORLEVEL% EQU 0 (
              docker compose -f ${params.COMPOSE_PATH} down --volumes --remove-orphans
            ) ELSE (
              docker-compose -f ${params.COMPOSE_PATH} down --volumes --remove-orphans
            )
          """
        } else {
          echo "ℹ️ Skipping Grid shutdown (we didn’t start it here)"
        }
      }
    }
    failure {
      echo "❌ Build failed - check console for details"
    }
  }
}
