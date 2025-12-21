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
        NVD_API_KEY = credentials('nvd-api-key')
    }
    steps {
        // Run the scan
        dependencyCheck additionalArguments: '''
        --scan .
        --format XML,HTML
        --out odc-report
        --data /var/lib/jenkins/odc-data
        ''', odcInstallation: 'dc'

        // Only publish XML (Jenkins parses XML, not HTML)
        dependencyCheckPublisher pattern: 'odc-report/dependency-check-report.xml'
    }
}

        stage('Sonar Quality Gate Scan'){
            steps{
                echo "Done Sonar Quality Gate Scan"
                }
            }
        
        stage('Trivy File System Scan'){
            steps {
                sh '''
                trivy fs --severity HIGH,CRITICAL \
                --format table \
                -o trivy-fs-report.txt .
                '''
            }
        }

        stage('Docker build'){
            steps{
                echo "Done Docker build"
            }
        }
    }
}
