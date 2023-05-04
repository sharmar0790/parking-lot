pipeline {
    agent any

    parameters {
        string(name: 'accountId', defaultValue: '', description: 'AWS Account ID')
        string(name: 'regionCode', defaultValue: 'eu-west-2', description: 'AWS Region Code')
        string(name: 'repoName', defaultValue: 'sample/dev/parking-lot', description: 'Repo Name')
        string(name: 'Build Version', defaultValue: '1.0', description: 'Build Version / Tag to be used.')
    }

    environment {
        registry = "${accountId}.dkr.ecr.${regionCode}.amazonaws.com"
        image_tag = "${version}"
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Clean & Build') {
            steps {

                sh "./mvnw clean install -DskipTests"
            }
        }

        stage('Docker Build Image') {
            steps {
                sh 'docker build -f ./config/Dockerfile -t ${repoName} .'
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('https://${registry}', 'ecr:${regionCode}:aws-credentials') {
                        sh 'docker tag ${repoName}:latest ${registry}:latest'
                        sh 'docker tag ${repoName}:latest ${registry}:${image_tag}'
                        sh 'docker push ${registry}:latest'
                        sh 'docker push ${registry}:${image_tag}'
                    }
                }
            }
        }

        stage('Cleaning up') {
            steps {
                sh 'docker rmi ${registry}:latest'
                sh 'docker rmi ${registry}:${image_tag}'
            }
        }
    }
}