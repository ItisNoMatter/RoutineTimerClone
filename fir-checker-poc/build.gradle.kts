plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
    testImplementation(libs.junit)
}
