# Jenkins Shared Library

Reusable Groovy shared library for Jenkins CI/CD pipelines.

---

## Structure

```
jenkins-shared-library/
├── src/
│   └── org/
│       └── devops/
│           ├── NotificationService.groovy  # Slack & Email notifications
│           └── DockerHelper.groovy         # Docker build & push
├── vars/
│   ├── notifySlack.groovy        # Global step: send Slack notification
│   ├── buildAndPushImage.groovy  # Global step: build & push Docker image
│   └── runSonarScan.groovy       # Global step: run SonarQube scan
└── README.md
```

---

## Classes (src/)

### NotificationService
Handles Slack and Email notifications.

```groovy
import org.devops.NotificationService

def notifier = new NotificationService(this)
notifier.sendSlack('Build passed!', 'good')
notifier.sendEmail('user@email.com', 'Build Status', 'Build passed!')
notifier.notifySuccess()
notifier.notifyFailure('Test Stage')
```

**Methods:**
- `sendSlack(message, color)` — Send Slack message
- `sendEmail(to, subject, body)` — Send email
- `notifySuccess()` — Send success notification
- `notifyFailure(stage)` — Send failure notification

---

### DockerHelper
Handles Docker image build and push operations.

```groovy
import org.devops.DockerHelper

def docker = new DockerHelper(this, 'myregistry.com')
docker.buildImage('myapp', 'v1.0')
docker.pushImage('myapp', 'v1.0')
docker.tagImage('myapp:v1.0', 'myapp:latest')
docker.removeImage('myapp', 'v1.0')
```

**Methods:**
- `buildImage(name, tag)` — Build Docker image
- `pushImage(name, tag)` — Push image to registry
- `tagImage(source, target)` — Tag an image
- `removeImage(name, tag)` — Remove local image

---

## Global Variables (vars/)

### notifySlack

Send a Slack notification.

```groovy
@Library('jenkins-shared-library') _

notifySlack(message: 'Build passed!', color: 'good')
notifySlack(message: 'Build failed!', color: 'danger')
notifySlack(message: 'Warning!',      color: 'warning')
```

**Parameters:**
| Parameter | Required | Default | Description |
|---|---|---|---|
| `message` | Yes | — | Message to send |
| `color` | No | `good` | good / danger / warning |

---

### buildAndPushImage

Build and push a Docker image.

```groovy
@Library('jenkins-shared-library') _

buildAndPushImage(
    name: 'flask-app',
    tag: 'v1.0',
    registry: '123456789.dkr.ecr.eu-north-1.amazonaws.com'
)
```

**Parameters:**
| Parameter | Required | Default | Description |
|---|---|---|---|
| `name` | Yes | — | Docker image name |
| `tag` | No | `latest` | Image tag |
| `registry` | No | `''` | Registry URL |

---

### runSonarScan

Run SonarQube static analysis.

```groovy
@Library('jenkins-shared-library') _

runSonarScan(
    projectKey: 'devops-flask-app',
    projectName: 'DevOps Flask App',
    sources: 'app',
    language: 'py'
)
```

**Parameters:**
| Parameter | Required | Default | Description |
|---|---|---|---|
| `projectKey` | Yes | — | SonarQube project key |
| `projectName` | Yes | — | SonarQube project name |
| `sources` | No | `.` | Source directory |
| `language` | No | `py` | Project language |

---

## How to Register in Jenkins

1. Go to **Manage Jenkins > System > Global Pipeline Libraries**
2. Click **Add**
3. Settings:
   - Name: `jenkins-shared-library`
   - Default version: `main`
   - Retrieval method: Modern SCM
   - Source Code Management: GitHub
   - Repository URL: `https://github.com/khushbakht-khan0/jenkins-shared-library.git`
4. Keep **Load implicitly** DISABLED
5. Click **Save**

---

## How to Use in Jenkinsfile

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent { label 'linux-agent' }
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
    }
    post {
        success {
            notifySlack(message: "Build passed: ${env.JOB_NAME} #${env.BUILD_NUMBER}", color: 'good')
        }
        failure {
            notifySlack(message: "Build failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}", color: 'danger')
        }
    }
}
```
