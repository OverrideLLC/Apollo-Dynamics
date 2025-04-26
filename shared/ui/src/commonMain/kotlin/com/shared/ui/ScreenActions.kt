package com.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenAction(
    icon: DrawableResource,
    name: String,
    size: DpSize = DpSize(width = 400.dp, height = 600.dp),
    close: () -> Unit,
    content: @Composable () -> Unit
) {
    val icon = painterResource(icon)
    val windowState = WindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition(Alignment.Center),
        size = size,
    )

    Window(
        onCloseRequest = { close() },
        icon = icon,
        title = name,
        state = windowState,
        resizable = true,
        undecorated = false,
        transparent = false,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}
