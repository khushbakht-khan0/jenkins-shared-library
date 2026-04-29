#!/usr/bin/env groovy
import org.devops.DockerHelper

/**
 * buildAndPushImage - Global variable to build and push Docker image
 *
 * Usage:
 *   buildAndPushImage(name: 'myapp', tag: 'latest', registry: 'myregistry')
 *
 * Required params:
 *   name (String) - Docker image name
 *
 * Optional params:
 *   tag      (String) - Image tag (default: 'latest')
 *   registry (String) - Docker registry URL (default: '')
 */
def call(Map params = [:]) {

    // Validate required params
    if (!params.name) {
        error("buildAndPushImage: 'name' parameter is required")
    }

    String name     = params.name
    String tag      = params.tag ?: 'latest'
    String registry = params.registry ?: ''

    def docker = new DockerHelper(this, registry)

    echo "=== BUILD AND PUSH IMAGE ==="
    echo "Name: ${name}, Tag: ${tag}"

    // Build image
    def fullName = docker.buildImage(name, tag)

    // Push image
    docker.pushImage(name, tag)

    echo "=== IMAGE PUSHED SUCCESSFULLY ==="
    return fullName
}
