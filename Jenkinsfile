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

        stage('Security Checks') {
            steps {
                echo 'Running Trivy security scan...'

                sh '''
                    mkdir -p .tools

                    curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b "$PWD/.tools" v0.76.1

                    ./.tools/trivy fs \
                        --scanners vuln \
                        --severity HIGH,CRITICAL \
                        --exit-code 1 \
                    .
                '''
            }
        }

        stage('Container Build') {
            steps {
                echo 'Building FinSecure Pay Docker image...'

                sh '''
                    set -e

                    docker build \
                    -t ghcr.io/louve-learns/finsecure-pay:build-${BUILD_NUMBER} \
                    .

                    docker images
                '''
            }
        }

    }
}
