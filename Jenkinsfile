pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'   // From Jenkins Global Tool Config
        jdk 'JDK_17'          // From Jenkins Global Tool Config
    }

    parameters {
        booleanParam(name: 'START_GRID', defaultValue: true, description: 'Start selenium grid via docker-compose before tests')
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

        stage('Start Grid') {
            when { expression { return params.START_GRID } }
            steps {
                echo "🚀 Starting Selenium Grid (docker-compose)..."
                // Run docker compose (v2) or docker-compose (v1) depending on what you have
                bat '''
                    docker compose version >nul 2>&1
                    IF %ERRORLEVEL% EQU 0 (
                      docker compose -f selenium-grid\\docker-compose.yml up -d
                    ) ELSE (
                      docker-compose -f selenium-grid\\docker-compose.yml up -d
                    )
                '''
                // small wait for hub & node to be ready
                bat 'powershell -Command "Start-Sleep -Seconds 10"'
            }
        }

        stage('Test') {
            steps {
                // Run TestNG suite on Selenium Grid
                bat '''
                    mvn test ^
                      -Dsurefire.suiteXmlFiles=testngaddressforextentreport.xml ^
                      -Denv=qa ^
                      -Dbrowser=chrome ^
                      -DrunOnGrid=true ^
                      -DseleniumGridUrl=http://localhost:4444/wd/hub ^
                      -Dheadless=true
                '''
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
                    bat '''
                        docker compose version >nul 2>&1
                        IF %ERRORLEVEL% EQU 0 (
                          docker compose -f selenium-grid\\docker-compose.yml down --volumes --remove-orphans
                        ) ELSE (
                          docker-compose -f selenium-grid\\docker-compose.yml down --volumes --remove-orphans
                        )
                    '''
                } else {
                    echo "START_GRID=false → skipping Grid shutdown"
                }
            }
        }
    }
}
