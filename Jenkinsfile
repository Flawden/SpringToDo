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
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Прогон тестов') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Сборка Docker-образа') {
            when {
                branch 'main'
            }
            steps {
                sh 'docker build -t $DOCKER_IMAGE:$BUILD_NUMBER .'
            }
        }

        stage('Push в DockerHub') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push $DOCKER_IMAGE:$BUILD_NUMBER
                        docker tag $DOCKER_IMAGE:$BUILD_NUMBER $DOCKER_IMAGE:latest
                        docker push $DOCKER_IMAGE:latest
                    '''
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
