#!/usr/bin/env groovy

/**
 * runSonarScan - Global variable to run SonarQube analysis
 *
 * Usage:
 *   runSonarScan(projectKey: 'my-project', projectName: 'My Project')
 *
 * Required params:
 *   projectKey  (String) - SonarQube project key
 *   projectName (String) - SonarQube project name
 *
 * Optional params:
 *   sources     (String) - Source directory (default: '.')
 *   language    (String) - Project language (default: 'py')
 */
def call(Map params = [:]) {

    // Validate required params
    if (!params.projectKey) {
        error("runSonarScan: 'projectKey' parameter is required")
    }
    if (!params.projectName) {
        error("runSonarScan: 'projectName' parameter is required")
    }

    String projectKey  = params.projectKey
    String projectName = params.projectName
    String sources     = params.sources ?: '.'
    String language    = params.language ?: 'py'

    echo "=== RUNNING SONARQUBE SCAN ==="
    echo "Project Key: ${projectKey}"
    echo "Project Name: ${projectName}"
    echo "Sources: ${sources}"

    withSonarQubeEnv('SonarQube') {
        sh """
            sonar-scanner \
                -Dsonar.projectKey=${projectKey} \
                -Dsonar.projectName=${projectName} \
                -Dsonar.sources=${sources} \
                -Dsonar.language=${language}
        """
    }

    echo "=== SONAR SCAN COMPLETE ==="
}
