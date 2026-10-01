folder('HW26') {
    description('CI/CD jobs for HW26 project')
}

/*
 * Build + test dla develop
 */
job('HW26/build-develop') {
    description('Build and test for develop branch')

    scm {
        git {
            remote {
                url('https://github.com/pkuzniak/HW26.git')
            }
            branch('*/develop')
        }
    }

    steps {
        shell('''
            echo "BUILD develop"
            echo "Running tests..."
            sleep 2
            echo "Tests passed"
        ''')
    }
}

/*
 * Build + test dla main
 * Jeśli w zadaniu masz master, możesz zmienić main -> master.
 */
job('HW26/build-main') {
    description('Build and test for main branch')

    scm {
        git {
            remote {
                url('https://github.com/pkuzniak/HW26.git')
            }
            branch('*/main')
        }
    }

    steps {
        shell('''
            echo "BUILD main"
            echo "Running tests..."
            sleep 2
            echo "Tests passed"
        ''')
    }
}

/*
 * Feature branches
 */
multibranchPipelineJob('HW26/build-features') {
    branchSources {
        git {
            id('hw26-features')
            remote('https://github.com/pkuzniak/HW26.git')

            includes('feature/*')
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath('Jenkinsfile')
        }
    }
}

/*
 * Deploy TEST
 */
job('HW26/deploy-test') {
    description('Deploy application to test environment')

    parameters {
        stringParam('APP_VERSION', '1.0.0', 'Version to deploy')
    }

    steps {
        shell('''
            echo "Deploying version $APP_VERSION to TEST"
            sleep 2
            echo "TEST deployment completed"
        ''')
    }
}

/*
 * Deploy PROD z ręcznym zatwierdzeniem
 */
pipelineJob('HW26/deploy-production') {
    description('Production deployment with manual approval')

    definition {
        cps {
            script('''
                pipeline {
                    agent any

                    parameters {
                        string(
                            name: 'APP_VERSION',
                            defaultValue: '1.0.0',
                            description: 'Version to deploy'
                        )
                    }

                    stages {

                        stage('Approval') {
                            steps {
                                input(
                                    message: "Deploy ${params.APP_VERSION} to production?",
                                    ok: 'Deploy'
                                )
                            }
                        }

                        stage('Deploy Production') {
                            steps {
                                echo "Deploying ${params.APP_VERSION} to PRODUCTION"

                                sh """
                                    echo "Production deployment started"
                                    sleep 2
                                    echo "Production deployment completed"
                                """
                            }
                        }
                    }
                }
            '''.stripIndent())
            sandbox()
        }
    }
}
