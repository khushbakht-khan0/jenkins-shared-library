package org.devops

class NotificationService implements Serializable {

    def script

    NotificationService(script) {
        this.script = script
    }

    def sendSlack(String message, String color = 'good') {
        try {
            script.withCredentials([script.string(credentialsId: 'slack-webhook', variable: 'SLACK_URL')]) {
                def payload = groovy.json.JsonOutput.toJson([
                    attachments: [[
                        color: color,
                        text: message,
                        fallback: message
                    ]]
                ])
                script.sh """curl -s -X POST -H 'Content-type: application/json' --data '${payload}' \${SLACK_URL}"""
            }
        } catch (Exception e) {
            script.echo "Slack notification failed: ${e.message}"
        }
    }
}