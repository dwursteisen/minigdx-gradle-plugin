package com.github.minigdx.gradle.plugin.internal

enum class MiniGdxPlatform(val banner: String, val pluginId: String) {
    JVM("banner-jvm.txt", "com.github.minigdx.jvm"),
    JAVASCRIPT("banner-js.txt", "com.github.minigdx.js"),
    ANDROID("banner-android.txt", "com.github.minigdx.android")
}
