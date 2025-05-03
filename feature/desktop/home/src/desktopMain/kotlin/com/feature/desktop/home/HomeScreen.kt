package com.feature.desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.feature.desktop.home.components.Content
import com.feature.desktop.home.components.TopBar
import com.shared.ui.BackgroundAnimated
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
) = Screen(
    workspace = { workspace() },
    toolsScreen = toolsScreen
)

@Composable
internal fun Screen(
    viewModel: HomeViewModel = koinViewModel(),
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        floatingActionButton = { },
        content = { padding ->
            Content(
                padding = padding,
                workspace = { workspace() },
                viewModel = viewModel,
                state = state,
                toolsScreen = toolsScreen
            )
        },
        topBar = { TopBar() },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = BackgroundAnimated(
                    colorBackground = colorScheme.background,
                    colorAnimated = colorScheme.onBackground
                )
            )
    )
}