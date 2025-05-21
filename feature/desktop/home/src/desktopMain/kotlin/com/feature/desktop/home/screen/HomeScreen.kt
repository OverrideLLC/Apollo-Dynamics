package com.feature.desktop.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.feature.desktop.home.components.Content
import com.feature.desktop.home.components.TopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        workspace = workspace,
        toolsScreen = toolsScreen,
        viewModel = viewModel
    )
}

@Composable
internal fun HomeScreen(
    state: HomeState,
    viewModel: HomeViewModel,
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
    onAction: (HomeAction) -> Unit,
) {
    Scaffold(
        floatingActionButton = { },
        topBar = { TopBar() },
        content = { padding ->
            Content(
                padding = padding,
                workspace = workspace,
                viewModel = viewModel,
                state = state,
                toolsScreen = toolsScreen
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    )
}