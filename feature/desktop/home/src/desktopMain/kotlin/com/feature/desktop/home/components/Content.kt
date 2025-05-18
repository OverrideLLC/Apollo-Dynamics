package com.feature.desktop.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.HomeViewModel
import com.feature.desktop.home.task.TaskRoot
import com.shared.ui.AcrylicCard

@Composable
internal fun Content(
    padding: PaddingValues,
    viewModel: HomeViewModel,
    state: HomeViewModel.HomeState,
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
) {
    val sizeTask by animateFloatAsState(
        targetValue = if (state.dockToLeft) 0.1f else 0.3f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
        label = "sizeTask"
    )

    val sizeWorkspace by animateFloatAsState(
        targetValue = if (state.dockToLeft && state.dockToRight) 0.8f else if (state.dockToLeft || state.dockToRight) 0.6f else 0.4f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
        label = "sizeWorkspace"
    )

    val sizeTools by animateFloatAsState(
        targetValue = if (state.dockToRight) 0.1f else 0.3f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
    )
    Column( // Cambia LazyColumn a Column
        modifier = Modifier
            .fillMaxSize()
            .padding(padding).padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Task(modifier = Modifier.weight(sizeTask).fillMaxHeight(), viewModel = viewModel)
            WorkSpace(
                modifier = Modifier.weight(sizeWorkspace).fillMaxHeight(),
                workspace = workspace
            )
            Tools(
                modifier = Modifier.weight(sizeTools).fillMaxHeight(),
                viewModel = viewModel,
                toolsScreen = toolsScreen
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
internal fun WorkSpace(
    workspace: @Composable () -> Unit,
    modifier: Modifier
) {
    AcrylicCard(
        modifier = modifier.fillMaxSize(),
        alpha = .8f,
        content = {
            workspace()
        }
    )
}

@Composable
internal fun Task(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    AcrylicCard(
        modifier = modifier.fillMaxSize(),
        alpha = .8f,
        content = {
            TaskRoot(
                onDockToLeft = { viewModel.dockToLeft() },
                isExpanded = viewModel.state.value.dockToLeft
            )
        }
    )
}

@Composable
internal fun Tools(
    modifier: Modifier,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
    viewModel: HomeViewModel
) {
    AcrylicCard(
        modifier = modifier.fillMaxSize(),
        alpha = .8f,
        content = {
            toolsScreen(viewModel.state.value.dockToRight) {
                viewModel.dockToRight()
            }
        }
    )
}
