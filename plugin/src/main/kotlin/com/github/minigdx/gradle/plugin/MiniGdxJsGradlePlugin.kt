package com.github.minigdx.gradle.plugin

import com.github.minigdx.gradle.plugin.internal.MiniGdxPlatform
import com.github.minigdx.gradle.plugin.internal.checkCommonPlugin
import com.github.minigdx.gradle.plugin.internal.createDir
import com.github.minigdx.gradle.plugin.internal.maybeCreateMiniGdxExtension
import com.github.minigdx.gradle.plugin.internal.minigdx
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip

class MiniGdxJsGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.maybeCreateMiniGdxExtension()
        project.checkCommonPlugin(MiniGdxPlatform.JAVASCRIPT)

        configureMiniGdxDependencies(project)

        project.createDir("src/jsMain/kotlin")
        project.createDir("src/jsTest/kotlin")

        configureTasks(project)
    }

    private fun configureTasks(project: Project) {
        project.tasks.register("runJs") {
            group = "minigdx"
            description = "Run your game in your browser."
            dependsOn("gltf", "jsBrowserDevelopmentRun")
        }

        project.tasks.register("bundleJs", Zip::class.java) {
            group = "minigdx"
            description = "Create a bundle as zip."
            dependsOn("gltf", "jsBrowserDistribution")
            from(project.buildDir.resolve("distributions"))
            destinationDirectory.set(project.buildDir.resolve("minigdx"))
            doLast {
                project.logger.lifecycle("[MINIGDX] The js distribution of your game is available at: ${outputs.files.first()}")
            }
        }

        // Copy resources from minigdx into the web distribution so it's available, as the web platform
        // can't access bundled resources.
        val copy = project.tasks.register("unpack-minigdx-resources", Copy::class.java) {
            val dependencies = project.configurations.getAt("minigdxToUnpack")

            group = "minigdx"
            description = "Unpack resources used by minigdx needed by the web platform."
            from(dependencies.map { project.zipTree(it) })
            include("/internal/**")
            into("build/processedResources/js/main")
        }
        project.afterEvaluate {
            project.tasks.getByName("jsProcessResources").dependsOn(copy)
        }
    }

    private fun configureMiniGdxDependencies(project: Project) {
        project.afterEvaluate {
            project.dependencies.add("commonMainImplementation", "com.github.minigdx:minigdx:${project.minigdx.version.get()}")
            project.dependencies.add("jsMainImplementation", "com.github.minigdx:minigdx-js:${project.minigdx.version.get()}")
            project.dependencies.add("minigdxToUnpack", "com.github.minigdx:minigdx-js:${project.minigdx.version.get()}")
        }
    }
}
