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
                implementation(libs.generativeai.google.wasm.js)
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
        }
    }
}
