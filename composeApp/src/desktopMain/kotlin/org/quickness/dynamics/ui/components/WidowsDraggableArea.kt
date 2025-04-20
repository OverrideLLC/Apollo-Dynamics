package org.quickness.dynamics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.shared.resources.Res
import com.shared.resources.check_box_outline_blank_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.close_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.remove_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp),
                        tint = colorScheme.tertiary
                    )
                }
            )
            Text(
                text = "TaskTec",
                modifier = Modifier.padding(start = 8.dp),
                color = colorScheme.tertiary,
                fontFamily = typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            IconButton(
                onClick = {},
                modifier = Modifier
                    .background(color = colorScheme.primary, shape = CircleShape)
                    .size(34.dp),
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize().padding(5.dp),
                        tint = Color.White,
                    )
                }
            )
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
                        tint = Color.Red
                    )
                }
            )
        }
    }
}