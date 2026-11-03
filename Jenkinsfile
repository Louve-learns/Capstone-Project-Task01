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
                    pwd
                    ls -la
                    test -f Dockerfile
                ''' 
                sh '''
                    set -e

                    docker build \
                    -t ghcr.io/louve-learns/finsecure-pay:build-${BUILD_NUMBER} \
                    .

                    docker images
                '''
            }
        }
        
        stage('Push to GHCR') {
            steps {
                echo 'Pushing FinSecure Pay image to GHCR...'

                withCredentials([
                    usernamePassword(
                        credentialsId: 'ghcr-credentials',
                        usernameVariable: 'GHCR_USERNAME',
                        passwordVariable: 'GHCR_PASSWORD'
                    )
                ]) {
                    sh '''
                        set +x
                        set -eu

                        export DOCKER_CONFIG="$(mktemp -d)"
                        trap 'rm -rf "$DOCKER_CONFIG"' EXIT

                        printf '%s' "$GHCR_PASSWORD" |
                            docker login ghcr.io \
                                -u "$GHCR_USERNAME" \
                                --password-stdin

                        docker push \
                            ghcr.io/louve-learns/finsecure-pay:build-${BUILD_NUMBER}
                    '''
                }
            }
        }

    }
}
