pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo 'Source code downloaded successfully!'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compiling FinSecure Pay application...'
                sh 'sh ./mvnw -B -DskipTests compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running JUnit 5 tests...'
                sh 'sh ./mvnw -B -DfailIfNoTests=true test'
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging FinSecure Pay application...'
                sh 'sh ./mvnw -B -DskipTests package'
                sh 'ls -lh target/*.jar'

                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

    }
}
