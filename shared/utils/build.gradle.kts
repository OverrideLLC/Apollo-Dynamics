plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    kotlin("plugin.serialization") version "2.1.21"
}
kotlin {

}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}
