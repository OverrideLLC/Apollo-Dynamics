package com.feature.desktop.home.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shared.resources.Res
import com.shared.resources.dock_to_left_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

@Composable
fun TasksScreen(
    onDockToLeft: () -> Unit
) = Screen(
    onDockToLeft = onDockToLeft
)

@Composable
internal fun Screen(
    onDockToLeft: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            IconButton(
                onClick = {
                    onDockToLeft()
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.dock_to_left_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Dock to left",
                        tint = colorScheme.primary
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .background(
                        color = colorScheme.onTertiary,
                        shape = shapes.small
                    )
            )
        }
    )
}