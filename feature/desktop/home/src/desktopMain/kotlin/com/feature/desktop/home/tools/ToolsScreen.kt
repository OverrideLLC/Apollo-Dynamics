package com.feature.desktop.home.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.feature.desktop.home.tools.navigation.NavigationTools
import com.feature.desktop.home.utils.enum.Tools
import com.shared.resources.Res
import com.shared.resources.dock_to_right_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ToolsScreen(
    isExpanded: Boolean,
    onDockToRight: () -> Unit
) = Screen(
    onDockToRight = onDockToRight,
    isExpanded = isExpanded
)

@Composable
internal fun Screen(
    isExpanded: Boolean,
    viewModel: ToolViewModel = koinViewModel(),
    onDockToRight: () -> Unit
) {
    val window = rememberWindowState(
        width = 720.dp,
        height = 300.dp,
        placement = WindowPlacement.Floating,
        position = WindowPosition(Alignment.Center)
    )
    val state by viewModel.state.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        onDockToRight()
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.dock_to_right_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "Dock to right",
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .align(
                            if (!isExpanded) Alignment.End else Alignment.CenterHorizontally
                        )
                        .padding(10.dp)
                        .background(
                            color = colorScheme.onTertiary,
                            shape = shapes.small
                        )
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (!isExpanded) Arrangement.Top else Arrangement.Center,
                    content = {
                        items(state.tools) { tool ->
                            ItemTool(
                                isExpanded = isExpanded,
                                tool = tool,
                                click = {
                                    viewModel.toolSelection(tool)
                                },
                            )
                        }
                    }
                )

                state.toolSelection?.let { tool ->
                    ScreenAction(
                        icon = tool.icon,
                        name = tool.name,
                        windowState = window,
                        close = { viewModel.toolSelection(null) },
                        content = { NavigationTools(tool.route) }
                    )
                } ?: run { }
            }
        }
    )
}

@Composable
fun ItemTool(
    isExpanded: Boolean,
    click: () -> Unit,
    tool: Tools
) {
    if (!isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { click() }
                .background(
                    color = colorScheme.onTertiary,
                    shape = shapes.small
                ),
            contentAlignment = Alignment.Center,
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(tool.icon),
                        contentDescription = tool.name,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = tool.nameString,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        )
    } else {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = colorScheme.background,
                    shape = shapes.small
                )
                .clickable { click() },
            contentAlignment = Alignment.Center,
            content = {
                Icon(
                    painter = painterResource(tool.icon),
                    contentDescription = tool.name,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}