pipeline {
    agent any

    parameters {
        string(name: 'accountId', defaultValue: '', description: 'AWS Account ID')
        string(name: 'regionCode', defaultValue: 'eu-west-2', description: 'AWS Region Code')
        string(name: 'repoName', defaultValue: 'sample/dev/parking-lot', description: 'Repo Name')
        string(name: 'Build Version', defaultValue: '1.0', description: 'Build Version / Tag to be used.')
    }

    environment {
        registry = "${accountId}.dkr.ecr.${regionCode}.amazonaws.com/${repoName}"
        image_tag = "${version}"
    }

    options {
        disableConcurrentBuilds()
        skipStagesAfterUnstable()
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
                sh 'aws ecr get-login-password --region $regionCode | docker login --username AWS --password-stdin $accountId.dkr.ecr.$regionCode.amazonaws.com'
                sh 'docker tag ${repoName}:latest ${registry}:latest'
                sh 'docker tag ${repoName}:latest ${registry}:${image_tag}'
                sh 'docker push ${registry}:latest'
                sh 'docker push ${registry}:${image_tag}'
            }
        }

        stage('Cleaning up') {
            steps {
                // cleanup current user docker credentials
                sh 'rm -f ~/.dockercfg ~/.docker/config.json || true'

                // cleanup the image stuffs
                sh 'docker rmi ${repoName}:latest'
                sh 'docker rmi ${ecr_repo_name}:latest'
                sh 'docker rmi ${ecr_repo_name}:${buildVersion}'

                // remove all the dangling images to clean up the space and resources
                sh 'docker rmi $(docker images --filter "dangling=true" -q --no-trunc)'
            }
        }
    }
}