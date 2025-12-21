pipeline{
    agent any
    
        tools {
        maven 'M3'  // Name of Maven installation configured in Jenkins for Sonar
    }
    
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
       
stage('OWASP Dependency Check') {
    environment {
        NVD_API_KEY = credentials('nvd-api-key')
    }
        tools {
        maven 'M3'  // Name of Maven installation configured in Jenkins
    }
    steps {
        // 1️⃣ Ensure output folder exists
        sh 'mkdir -p odc-report'

        // 2️⃣ Build Maven project and copy dependencies
        sh 'mvn clean compile dependency:copy-dependencies'

        // 3️⃣ Run Dependency-Check (HTML + XML via ALL)
        dependencyCheck additionalArguments: '''
        --scan target/dependency
        --format ALL
        --out odc-report
        --data /var/lib/jenkins/odc-data
        ''', odcInstallation: 'dc'

        // 4️⃣ Publish XML report in Jenkins
        dependencyCheckPublisher pattern: 'odc-report/dependency-check-report.xml'
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

        stage('SonarQube Quality Analysis') {
            environment {
                SONAR_TOKEN = credentials('Sonar')
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn clean verify sonar:sonar \
                    -Dsonar.sources=src/main/java
                    -Dsonar.projectKey=sonar-demo-app \
                    -Dsonar.projectName=sonar-demo-app \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Sonar Quality Gate Scan') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
