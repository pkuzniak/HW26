pipeline {
    agent any

    environment {
        APP_VERSION = '1.0.0'
        DEPLOY_ENV = 'staging'
        APP_NAME = 'demo-app'
        BUILD_OK = 'false'
        TEST_OK = 'false'
    }

    stages {

        stage('Checkout') {
            steps {
                script {
                    try {
                        echo 'Checkout repository'
                        checkout scm
                    } catch (Exception e) {
                        echo "Checkout error: ${e}"
                        currentBuild.result = 'FAILURE'
                        error('Checkout failed')
                    }
                }
            }
        }

stage('Build') {
    steps {
        script {
            try {
                echo "Build started"

                sh 'exit 1'

                env.BUILD_OK = 'true'
            } catch (Exception e) {
                env.BUILD_OK = 'false'
                echo "Build error: ${e}"
                currentBuild.result = 'FAILURE'
                error('Build failed')
            }
        }
    }
}
        stage('Test') {
            when {
                expression {
                    env.BUILD_OK == 'true'
                }
            }

            steps {
                script {
                    try {
                        echo 'Running tests'

                        sh '''
                            echo "Tests started"
                            sleep 2
                            echo "Tests passed"
                        '''

                        env.TEST_OK = 'true'

                    } catch (Exception e) {
                        env.TEST_OK = 'false'
                        echo "Test error: ${e}"
                        currentBuild.result = 'FAILURE'
                        error('Tests failed')
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                allOf {
                    expression {
                        env.BUILD_OK == 'true'
                    }

                    expression {
                        env.TEST_OK == 'true'
                    }

                    expression {
                        env.DEPLOY_ENV == 'staging'
                    }
                }
            }

            steps {
                script {
                    try {
                        echo "Deploying ${APP_NAME}"
                        echo "Version: ${APP_VERSION}"
                        echo "Environment: ${DEPLOY_ENV}"

                        sh '''
                            echo "Deployment started"
                            sleep 2
                            echo "Deployment completed"
                        '''

                    } catch (Exception e) {
                        echo "Deploy error: ${e}"
                        currentBuild.result = 'FAILURE'
                        error('Deploy failed')
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'PIPELINE SUCCESS'
        }

        failure {
            echo 'PIPELINE FAILURE'
        }

        unstable {
            echo 'PIPELINE UNSTABLE'
        }

        always {
            echo "Final status: ${currentBuild.currentResult}"
        }
    }
}
