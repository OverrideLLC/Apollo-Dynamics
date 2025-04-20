package com.feature.desktop.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.components.Content
import com.feature.desktop.home.components.TopBar
import com.shared.ui.BackgroundAnimated

@Composable
fun HomeScreen(
    navigationHome: @Composable (navController: NavHostController) -> Unit
) = Screen(navigationHome = navigationHome)

@Composable
internal fun Screen(
    navigationHome: @Composable (navController: NavHostController) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = { },
        content = { padding -> Content(padding = padding) },
        topBar = { TopBar() },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = BackgroundAnimated(
                    colorBackground = colorScheme.background,
                    colorAnimated = colorScheme.primary.copy(alpha = 0.7f)
                )
            )
    )
}