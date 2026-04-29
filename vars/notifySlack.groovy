#!/usr/bin/env groovy
import org.devops.NotificationService

/**
 * notifySlack - Global variable to send Slack notifications
 *
 * Usage:
 *   notifySlack(message: 'Build passed!', color: 'good')
 *   notifySlack(message: 'Build failed!', color: 'danger')
 *
 * Required params:
 *   message (String) - Message to send
 *
 * Optional params:
 *   color (String) - 'good', 'danger', 'warning' (default: 'good')
 */
def call(Map params = [:]) {

    // Validate required params
    if (!params.message) {
        error("notifySlack: 'message' parameter is required")
    }

    String message = params.message
    String color   = params.color ?: 'good'

    def notifier = new NotificationService(this)
    notifier.sendSlack(message, color)
}
