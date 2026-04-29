package org.devops

class NotificationService implements Serializable {

    private def script
    private String jobName
    private String buildNumber
    private String buildUrl

    // Constructor
    NotificationService(def script) {
        this.script = script
        this.jobName = script.env.JOB_NAME ?: 'Unknown Job'
        this.buildNumber = script.env.BUILD_NUMBER ?: '0'
        this.buildUrl = script.env.BUILD_URL ?: '#'
    }

    // Send Slack notification
    def sendSlack(String message, String color = 'good') {
        script.echo "=== SLACK NOTIFICATION ==="
        script.echo "Color: ${color}"
        script.echo "Message: ${message}"
        script.echo "Job: ${jobName} #${buildNumber}"
        script.echo "URL: ${buildUrl}"
        script.echo "=========================="

        // Agar Slack plugin configured ho toh:
        // script.slackSend(color: color, message: message)
    }

    // Send Email notification
    def sendEmail(String to, String subject, String body) {
        if (!to) {
            script.echo "ERROR: Email 'to' field is required"
            return
        }
        if (!subject) {
            script.echo "ERROR: Email 'subject' field is required"
            return
        }

        script.echo "=== EMAIL NOTIFICATION ==="
        script.echo "To: ${to}"
        script.echo "Subject: ${subject}"
        script.echo "Body: ${body}"
        script.echo "=========================="
    }

    // Send success notification
    def notifySuccess() {
        def message = "✅ BUILD SUCCESS: ${jobName} #${buildNumber} | ${buildUrl}"
        sendSlack(message, 'good')
    }

    // Send failure notification
    def notifyFailure(String failedStage = 'Unknown') {
        def message = "❌ BUILD FAILED: ${jobName} #${buildNumber} | Stage: ${failedStage} | ${buildUrl}"
        sendSlack(message, 'danger')
    }
}
