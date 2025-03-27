package org.quickness.dynamics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import org.jetbrains.compose.resources.painterResource
import org.quickness.dynamics.ui.theme.onBackground
import org.quickness.dynamics.ui.theme.tertiary
import quicknessdynamics.composeapp.generated.resources.LogoBlancoQuickness
import quicknessdynamics.composeapp.generated.resources.Res
import quicknessdynamics.composeapp.generated.resources.check_box_outline_blank_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.close_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.remove_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24

@Composable
fun TopWindows(
    windowState: WindowState,
    exitApplication: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(38.dp)
            .padding(start = 8.dp)
            .background(colorScheme.onBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(Res.drawable.LogoBlancoQuickness),
            contentDescription = "Logo",
            modifier = Modifier.height(24.dp),
            tint = colorScheme.tertiary
        )
        Row {
            IconButton(
                onClick = {
                    windowState.isMinimized = true
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.remove_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Minimize",
                        modifier = Modifier.height(24.dp),
                        tint = colorScheme.tertiary
                    )
                }
            )
            IconButton(
                onClick = {
                    windowState.placement =
                        if (windowState.placement == WindowPlacement.Maximized)
                            WindowPlacement.Floating
                        else
                            WindowPlacement.Maximized
                },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.check_box_outline_blank_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Maximize",
                        modifier = Modifier.height(24.dp),
                        tint = colorScheme.tertiary
                    )
                }
            )
            IconButton(
                onClick = { exitApplication() },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.close_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Close",
                        modifier = Modifier.height(24.dp),
                        tint = colorScheme.tertiary
                    )
                }
            )
        }
    }
}