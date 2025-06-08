pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "flawden/spring-todo"
    }

    stages {
        stage('Клонирование репозитория') {
            steps {
                checkout scm
            }
        }

        stage('Сборка проекта') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean install -DskipTests'
                    } else {
                        bat 'mvn clean install -DskipTests'
                    }
                }
            }
        }

        stage('Прогон тестов') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Сборка Docker-образа') {
            when {
                branch 'main'
            }
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .'
                    } else {
                        bat "docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% ."
                    }
                }
            }
        }

        stage('Push в DockerHub') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        if (isUnix()) {
                            sh '''
                                echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                                docker push $DOCKER_IMAGE:$BUILD_NUMBER
                                docker tag $DOCKER_IMAGE:$BUILD_NUMBER $DOCKER_IMAGE:latest
                                docker push $DOCKER_IMAGE:latest
                            '''
                        } else {
                            bat """
                                echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                                docker push %DOCKER_IMAGE%:%BUILD_NUMBER%
                                docker tag %DOCKER_IMAGE%:%BUILD_NUMBER% %DOCKER_IMAGE%:latest
                                docker push %DOCKER_IMAGE%:latest
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
