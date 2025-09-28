pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'   // From Jenkins Global Tool Config
        jdk 'JDK_17'          // From Jenkins Global Tool Config
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-repo>.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                // Run TestNG suite locally
                bat '''
                    mvn test ^
                      -Dsurefire.suiteXmlFiles=testng.xml ^
                      -Denv=dev ^
                      -Dbrowser=chrome ^
                      -DrunOnGrid=false ^
                      -Dheadless=false
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
        }
    }
}
