package com.feature.desktop.home.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shared.resources.Res
import com.shared.resources.dock_to_left_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.dock_to_right_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

@Composable
fun ToolsScreen(
    onDockToRight: () -> Unit
) = Screen(
    onDockToRight = onDockToRight
)

@Composable
internal fun Screen(
    onDockToRight: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            IconButton(
                onClick = {
                    onDockToRight()
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.dock_to_right_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Dock to right",
                        tint = colorScheme.primary
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(
                        color = colorScheme.onBackground.copy(alpha = 0.7f),
                        shape = shapes.small
                    )
            )
        }
    )
}