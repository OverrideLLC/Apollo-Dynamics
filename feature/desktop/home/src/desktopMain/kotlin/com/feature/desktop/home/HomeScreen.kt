package com.feature.desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.Content
import com.feature.desktop.home.components.TopBar
import com.shared.resources.Res
import com.shared.resources.school_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.BackgroundAnimated
import com.shared.ui.ScreenAction
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    workspace: @Composable () -> Unit,
    toolsScreen: @Composable (Boolean, () -> Unit) -> Unit,
    servicesScreen: @Composable () -> Unit
) = Screen(
    workspace = { workspace() },
    servicesScreen = servicesScreen,
    toolsScreen = toolsScreen
)

@Composable
internal fun Screen(
    viewModel: HomeViewModel = koinViewModel(),
    workspace: @Composable () -> Unit,
    servicesScreen: @Composable () -> Unit,
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
        topBar = {
            TopBar(
                onClick = {
                    viewModel.serviceSelected(it)
                }
            )
        },
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

    state.serviceSelected?.let {
        ScreenAction(
            icon = it.icon,
            name = it.nameServices,
            size = DpSize(800.dp, 600.dp),
            close = { viewModel.serviceSelected(null) },
            content = { servicesScreen() }
        )
    } ?: run {  }
}