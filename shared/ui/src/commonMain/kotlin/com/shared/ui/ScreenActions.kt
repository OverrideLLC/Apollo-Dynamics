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
    resizable: Boolean = true,
    undecorated: Boolean = false,
    transparent: Boolean = false,
    size: DpSize = DpSize(width = 400.dp, height = 600.dp),
    placement: WindowPlacement = WindowPlacement.Floating,
    close: () -> Unit,
    content: @Composable () -> Unit
) {
    val icon = painterResource(icon)
    val windowState = WindowState(
        placement = placement,
        position = WindowPosition(Alignment.Center),
        size = size,
    )

    Window(
        onCloseRequest = { close() },
        icon = icon,
        title = name,
        state = windowState,
        resizable = resizable,
        undecorated = undecorated,
        transparent = transparent,
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
