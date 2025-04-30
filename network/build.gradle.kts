plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    sourceSets {
        val desktopMain by getting

        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.generativeai)
                api(libs.generativeai.google.wasm.js)
                implementation(libs.koin.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("com.google.firebase:firebase-admin:9.4.3")
            implementation("com.google.guava:guava:31.1-jre")
            implementation(project.dependencies.platform(libs.google.cloud.bom))
            implementation(libs.google.cloud.firestore)
            implementation(libs.google.cloud.secretmanager)
        }
    }
}
