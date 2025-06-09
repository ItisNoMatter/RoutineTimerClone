// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" apply false
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
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
