pipeline{
    agent any
    environment{
        SONAR_HOME= tool "Sonar"
    }
    stages{
        stage("Clone Code from GitHub"){
            steps{
                git url: "https://github.com/SudarshanKDeore/My_Docker.git", branch: "main"
            }
        }
        stage("SonarQube Quality Analysis"){
            steps{
                echo "Done SonarQube Quality Analysis"
                }
            }
        
        stage("OWASP Dependency Check"){
            steps{
                dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'dc'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Sonar Quality Gate Scan"){
            steps{
                echo "Done Sonar Quality Gate Scan"
                }
            }
        
        stage("Trivy File System Scan"){
            steps{
                sh "trivy fs --format  table -o trivy-fs-report.html ."
            }
        }
        stage("Docker build"){
            steps{
                echo "Done Docker build"
            }
        }
    }
}