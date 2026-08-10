plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    kotlinCompilerPluginClasspath(project(":fir-checker-poc"))
}
