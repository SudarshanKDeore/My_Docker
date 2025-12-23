1) SonarQube
# docker run --name sonarqube-custom -p 9000:9000 sonarqube:lts
 ID : Sonar
--------------------Jenkins Step-------------------

        stage('SonarQube Quality Analysis') {
            environment {
                SONAR_TOKEN = credentials('Sonar')
            }

        tools {
        maven 'M3'  // Name of Maven installation configured in Jenkins for Sonar
        }

            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn clean verify sonar:sonar \
                    -Dsonar.sources=src/main/java \              # Source to Scan
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
                    waitForQualityGate abortPipeline: true	  # Fail when Bugs,Vulnerabilities Found
                }
            }
        }
------------------
Check Output: On SonarQube Server : <Public_IP>:9000
-------------------------------------------------------------------------------------------------------

2) OWASP
		Add in Jenkins: this API key Created on nvd.nist.gov
		Kind: Secret text
		Secret: 157e9d87-1303-4763-b2d3-39284816e8f0   (API Key)
		ID: nvd-api-key
    Add Jenkins Plugin : OWASP Dependency-Check
   
---------------------Jenkins Step----------------------

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
----------------------
Check Output:
cat /var/lib/jenkins/workspace/<Jenkins Project Name>/odc-report/dependency-check-report.xml
cat /var/lib/jenkins/workspace/<Jenkins Project Name>/odc-report/dependency-check-report.html
OR : Go to Jenkins console -then project -then project workspace -then odc-report -then click on dependency-check-report.html     --> then see the output on console
-----------------------------------------------------------------------------------------------------------------

3) Trivy Installation on EC2

✔ Step-1: Install required packages
sudo apt-get install wget apt-transport-https gnupg lsb-release -y

✔ Step-2: Add the Trivy GPG key (WITHOUT apt-key)
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key \
  | sudo gpg --dearmor -o /usr/share/keyrings/trivy.gpg

✔ Step-3: Add the Trivy repo using signed-by
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb \
$(lsb_release -sc) main" \
 | sudo tee /etc/apt/sources.list.d/trivy.list

✔ Step-4: Update & Install Trivy
sudo apt-get update -y
sudo apt-get install trivy -y

------------------------Trivy Jenkins step----------------------

        stage('Trivy File System Scan'){
            steps {
                sh '''
                trivy fs --severity HIGH,CRITICAL \
                --format table \
                -o trivy-fs-report.txt .
                '''
            }
        }
-----------------------

Check Output : cat /var/lib/jenkins/workspace/<Jenkins Project Name>/trivy-fs-report.txt
           OR less /var/lib/jenkins/workspace/<Jenkins Project Name>/trivy-fs-report.txt
-----------------------------------------------------------------------------------------------------------------
