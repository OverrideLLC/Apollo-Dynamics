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
                //Modules
                implementation(projects.shared.utils)
                implementation(projects.shared.ui)
                implementation(projects.shared.resources)
                implementation(projects.network)

                implementation(libs.kotlin.stdlib)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.androidx.navigation)
                implementation(libs.qr.kit)
                implementation(libs.koalaplot.core)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.composeVM)
                implementation(compose.uiTooling)

                //UTILS
                implementation("org.commonmark:commonmark:0.24.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
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
