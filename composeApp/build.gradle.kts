import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(projects.feature.desktop.api)
            implementation(projects.shared.resources)
            implementation(projects.network)
            implementation(projects.data)
        }
        commonMain.dependencies {
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
            implementation("com.google.firebase:firebase-admin:9.4.3")
            implementation("com.google.guava:guava:31.1-jre")
            implementation(project.dependencies.platform(libs.google.cloud.bom))
            implementation(libs.google.cloud.firestore)
            implementation(libs.google.cloud.secretmanager)
        }
    }
}
compose.desktop {
    application {
        mainClass = "org.quickness.dynamics.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.quickness.dynamics"
            packageVersion = "1.0.0"
        }
    }
}