// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Root build.gradle.kts
buildscript {
    dependencies {
        classpath(libs.google.services) // Ensure latest version
    }
}
