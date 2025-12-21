pipeline{
    agent any

    stages{
    
        stage('Clean Workspace') {
            steps {
                cleanWs()  // Clean before starting
            }
        }
    
        stage('Clone Code from GitHub'){
            steps {
                git credentialsId: 'github-creds',
                    url: 'https://github.com/SudarshanKDeore/My_Docker.git',
                    branch: 'main'
            }
        }

        stage('SonarQube Quality Analysis'){
            steps{
                echo "Done SonarQube Quality Analysis"
                }
            }
        
stage('OWASP Dependency Check') {
        environment {
        DC_NVD_API_KEY = credentials('nvd-api-key')
    }
    steps {
        withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
            dependencyCheck additionalArguments: """
            --scan .
            --format XML
            --out ./odc-report
            --data /var/lib/jenkins/odc-data
            --nvdApiKey ${NVD_API_KEY}
            """, odcInstallation: 'dc'
        }

        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
    }
}


        stage('Sonar Quality Gate Scan'){
            steps{
                echo "Done Sonar Quality Gate Scan"
                }
            }
        
        stage('Trivy File System Scan'){
            steps{
                sh "trivy fs --format  table -o trivy-fs-report.html ."
            }
        }

        stage('Docker build'){
            steps{
                echo "Done Docker build"
            }
        }
    }
}
