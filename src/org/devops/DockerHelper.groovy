package org.devops

class DockerHelper implements Serializable {

    private def script
    private String registry

    // Constructor
    DockerHelper(def script, String registry = '') {
        this.script = script
        this.registry = registry
    }

    // Build Docker image
    def buildImage(String name, String tag = 'latest') {
        if (!name) {
            script.error("ERROR: Image name is required")
        }

        def fullName = registry ? "${registry}/${name}:${tag}" : "${name}:${tag}"

        script.echo "=== BUILDING DOCKER IMAGE ==="
        script.echo "Image: ${fullName}"
        script.sh "docker build -t ${fullName} ."
        script.echo "Build complete: ${fullName}"

        return fullName
    }

    // Push Docker image
    def pushImage(String name, String tag = 'latest') {
        if (!name) {
            script.error("ERROR: Image name is required")
        }

        def fullName = registry ? "${registry}/${name}:${tag}" : "${name}:${tag}"

        script.echo "=== PUSHING DOCKER IMAGE ==="
        script.echo "Image: ${fullName}"
        script.sh "docker push ${fullName}"
        script.echo "Push complete: ${fullName}"
    }

    // Tag image
    def tagImage(String source, String target) {
        script.echo "Tagging: ${source} -> ${target}"
        script.sh "docker tag ${source} ${target}"
    }

    // Remove local image
    def removeImage(String name, String tag = 'latest') {
        def fullName = "${name}:${tag}"
        script.echo "Removing image: ${fullName}"
        script.sh "docker rmi ${fullName} || true"
    }
}
