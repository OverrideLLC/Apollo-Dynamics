package com.feature.desktop.api.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.feature.desktop.home.HomeScreen
import com.feature.desktop.home.ai.ui.screen.AiScreen
import com.feature.desktop.start.screen.StartScreen
import com.shared.utils.routes.RoutesStart

@Composable
fun NavigationStart(navController: NavHostController) {
    NavHost(navController, startDestination = RoutesStart.Home.route) {
        composable(RoutesStart.Start.route) { StartScreen(navController) }
        composable(RoutesStart.Home.route) { HomeScreen(workspace = { AiScreen() }) }
    }
}