package com.feature.desktop.api.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.ai.ui.screen.AiScreen
import com.feature.desktop.home.screen.HomeRoot
import com.feature.desktop.home.tools.ui.ToolsScreen
import com.shared.utils.routes.RoutesHome

@Composable
fun NavigationHome() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RoutesHome.Dashboard.route,
    ) {
        composable(RoutesHome.Dashboard.route) {
            HomeRoot(
                toolsScreen = { isExpanded, onDockToRight ->
                    ToolsScreen(
                        isExpanded = isExpanded,
                        onDockToRight = onDockToRight
                    )
                },
                workspace = { AiScreen() },
            )
        }
    }
}