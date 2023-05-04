pipeline {
    agent any

    parameters {
        string(name: 'accountId', defaultValue: '', description: 'AWS Account ID')
        string(name: 'regionCode', defaultValue: 'eu-west-2', description: 'AWS Region Code')
        string(name: 'repoName', defaultValue: 'sample/dev/parking-lot', description: 'Repo Name')
        string(name: 'buildVersion', defaultValue: '1.5', description: 'Build Version / Tag to be used.')
    }

    environment {
        registry = "${accountId}.dkr.ecr.${regionCode}.amazonaws.com"
        ecr_repo_name = "${registry}/${repoName}"
        aws_credential_id = "aws-credentials"
        registry_credential = "ecr:${regionCode}:${aws_credential_id}"
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
                script {
                    docker.withRegistry('https://${registry}', "${registry_credential}") {
                        sh 'docker tag ${repoName}:latest ${ecr_repo_name}:latest'
                        sh 'docker tag ${repoName}:latest ${ecr_repo_name}:${buildVersion}'
                        sh 'docker push ${ecr_repo_name}:latest'
                        sh 'docker push ${ecr_repo_name}:${buildVersion}'
                    }
                }
            }
        }

        stage('Integrate Jenkins with EKS Cluster and Deploy App') {
            steps {
                withAWS(credentials: '<AWS_CREDENTIALS_ID>', region: '<AWS_REGION>') {
                    script {
                        sh('aws eks update-kubeconfig --name <EKS_CLUSTER_NAME> --region <AWS_REGION>')
                        sh "kubectl apply -f <K8S_DEPLOY_FILE>.yaml"
                    }
                }
            }
        }

        stage('Clean up') {
            steps {
                sh 'docker rmi ${repoName}:latest'
                sh 'docker rmi ${ecr_repo_name}:latest'
                sh 'docker rmi ${ecr_repo_name}:${buildVersion}'

                // remove all the dangling images to clean up the space and resources
                sh 'docker rmi $(docker images --filter "dangling=true" -q --no-trunc)'

            }
        }
    }
}