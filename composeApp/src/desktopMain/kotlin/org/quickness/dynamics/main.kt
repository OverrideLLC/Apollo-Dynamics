package org.quickness.dynamics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.api.navhost.NavigationStart
import com.network.init.initGcloudFromPath
import com.shared.resources.Res
import com.shared.resources.TTNegro
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinContext
import org.quickness.dynamics.di.initKoin
import org.quickness.dynamics.ui.theme.QuicknessDynamicsTheme

fun main() = application {
    initKoin()
    initGcloudFromPath("/home/christopher-cop787-gmail-com/Documentos/Credentials/Google Cloud/quickness-backend-gc.json")
    val viewModel = ApplicationViewModel()
    val state by viewModel.state.collectAsState()
    val windowState = rememberWindowState(
        width = 1280.dp,
        height = 720.dp,
        placement = WindowPlacement.Maximized
    )

    Window(
        onCloseRequest = { viewModel.update { copy(isOpened = false) } },
        icon = painterResource(Res.drawable.TTNegro),
        title = state.name,
        state = windowState,
        resizable = true,
        undecorated = false,
        transparent = false,
    ) {
        KoinContext {
            QuicknessDynamicsTheme(
                darkTheme = !state.darkTheme
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().background(colorScheme.background),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationStart(
                        navController = rememberNavController()
                    )
                }
            }
        }
    }
}