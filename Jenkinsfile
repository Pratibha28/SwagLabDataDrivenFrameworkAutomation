pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'   // From Jenkins Global Tool Config
        jdk 'JDK_17'          // From Jenkins Global Tool Config
    }

    parameters {
        booleanParam(name: 'START_GRID', defaultValue: false, description: 'Start selenium grid via docker-compose before tests')
        string(name: 'COMPOSE_PATH', defaultValue: 'selenium-grid\\docker-compose.yml', description: 'Path to docker-compose file relative to workspace')
        string(name: 'SELENIUM_GRID_URL', defaultValue: 'http://localhost:4444/wd/hub', description: 'Grid URL to run tests against')
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

        stage('Start Grid (optional)') {
            when { expression { return params.START_GRID } }
            steps {
                echo "🚀 Starting Selenium Grid from ${params.COMPOSE_PATH}"
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
                    echo "🛑 Stopping Selenium Grid..."
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
    }
}
