// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // FIX: Force the correct JavaPoet version to prevent Hilt plugin crash
        classpath("com.squareup:javapoet:1.13.0")

        // Note: You do not need to add the Hilt classpath here if you are using
        // the plugins block below, but this specific library fix must happen here.
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Make sure Hilt is defined here too if you have it in your libs.versions.toml,
    // otherwise ensure it's applied in the app module.
    alias(libs.plugins.hilt) apply false
}