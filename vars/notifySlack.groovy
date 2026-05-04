def call(Map params = [:]) {
    if (!params.message) {
        error("notifySlack: 'message' parameter is required")
    }

    String message = params.message
    String color   = params.color ?: 'good'

    try {
        withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_URL')]) {
            sh """
                curl -s -X POST -H 'Content-type: application/json' \
                --data '{"attachments":[{"color":"${color}","text":"${message}"}]}' \
                \$SLACK_URL
            """
        }
    } catch (Exception e) {
        echo "Slack notification skipped: ${e.message}"
    }
}