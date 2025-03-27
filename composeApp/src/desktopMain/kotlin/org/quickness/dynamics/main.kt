package org.quickness.dynamics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.api.NavigationStart
import org.jetbrains.compose.resources.painterResource
import org.quickness.dynamics.ui.components.TopWindows
import org.quickness.dynamics.ui.theme.QuicknessDynamicsTheme
import quicknessdynamics.composeapp.generated.resources.LogoBlancoQuickness
import quicknessdynamics.composeapp.generated.resources.Res

fun main() = application {
    val windowState = rememberWindowState(
        width = 1280.dp,
        height = 720.dp,
        placement = WindowPlacement.Maximized
    )
    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.LogoBlancoQuickness),
        title = "Quickness Dynamics",
        state = windowState,
        resizable = true,
        undecorated = true,
    ) {
        QuicknessDynamicsTheme(
            darkTheme = true
        ){
            Column(
                modifier = Modifier.fillMaxSize().background(colorScheme.background),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopWindows(windowState = windowState) { windowState.isMinimized = true }
                NavigationStart(
                    navController = rememberNavController()
                )
            }
        }
    }
}