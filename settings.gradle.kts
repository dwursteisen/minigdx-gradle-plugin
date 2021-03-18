/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/6.8.2/userguide/multi_project_builds.html
 */

rootProject.name = "minigdx-gradle-plugin"
include("plugin")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }.mavenContent {
            includeVersionByRegex("com.github.minigdx.(.*)", "(.*)", "LATEST-SNAPSHOT")
            includeVersionByRegex("com.github.minigdx", "(.*)", "LATEST-SNAPSHOT")
        }
        mavenLocal()
        jcenter()
    }
    // Replace the plugin com.android.application with a dependency with another name.
    // See https://medium.com/@StefMa/its-time-to-ditch-the-buildscript-block-a1ab12e0d9ce
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:3.6.1")
            }
        }
    }
}