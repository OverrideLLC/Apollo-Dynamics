package com.feature.desktop.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.start.StartScreen
import com.shared.utils.routes.RoutesStart

@Composable
fun NavigationStart(navController: NavHostController) {
    NavHost(navController, startDestination = RoutesStart.Start.route) {
        composable(RoutesStart.Start.route) { StartScreen(rememberNavController()) }
    }
}