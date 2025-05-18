package com.feature.desktop.home.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.task.components.TaskComponent
import com.shared.resources.Res
import com.shared.resources.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.dock_to_left_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskRoot(
    viewModel: TaskViewModel = koinViewModel(),
    onDockToLeft: () -> Unit,
    isExpanded: Boolean
) {
    val state by viewModel.state.collectAsState()

    TaskScreen(
        state = state,
        isExpanded = isExpanded,
        onDockToLeft = onDockToLeft,
        onAction = viewModel::onAction
    )
}

@Composable
fun TaskScreen(
    isExpanded: Boolean,
    state: TaskState,
    onAction: (TaskAction) -> Unit,
    onDockToLeft: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.dock_to_left_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "Dock to left",
                            tint = colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = colorScheme.onTertiary,
                            shape = shapes.small
                        )
                )
                IconButton(
                    onClick = {
                        onDockToLeft()
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "Add Task",
                            tint = colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(
                            color = colorScheme.onTertiary,
                            shape = shapes.small
                        )
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (!isExpanded) Arrangement.Top else Arrangement.Center,
                content = {
                    items(state.tasks) { task ->
                        TaskComponent(
                            title = task.title,
                            description = task.description,
                            isExpanded = !isExpanded
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            )
        }
    )
}