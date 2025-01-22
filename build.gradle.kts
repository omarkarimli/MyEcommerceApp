buildscript {
    repositories {
        google()
    }
    dependencies {
        val navVersion = "2.8.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false

    // Dagger Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}