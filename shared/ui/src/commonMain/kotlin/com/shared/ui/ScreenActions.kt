package com.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenAction(
    icon: DrawableResource,
    name: String,
    windowState: WindowState,
    close: () -> Unit,
    content: @Composable () -> Unit
) {
    val icon = painterResource(icon)
    Window(
        onCloseRequest = { close() },
        icon = icon,
        title = name,
        state = windowState,
        resizable = false,
        undecorated = true,
        transparent = true,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WindowDraggableArea(
                content = {
                    TopWindows {
                        close()
                    }
                }
            )
            content()
        }
    }
}

@Composable
fun TopWindows(exitApplication: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { exitApplication() },
                modifier = Modifier
                    .background(
                        color = if (isHovered) colorScheme.error else Color.Transparent,
                        shape = CircleShape
                    )
                    .size(34.dp)
                    .hoverable(
                        interactionSource = interactionSource,
                    ),
                content = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.height(24.dp),
                        tint = colorScheme.primary
                    )
                }
            )
        }
    }
}