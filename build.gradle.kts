// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}
allprojects {
    configurations.all {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.squareup:javapoet:1.13.0")
    }
    configurations.classpath {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }
}
