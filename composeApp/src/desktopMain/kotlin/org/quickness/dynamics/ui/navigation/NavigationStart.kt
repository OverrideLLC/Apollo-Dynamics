package org.quickness.dynamics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.quickness.dynamics.ui.screens.home.HomeScreen
import org.quickness.dynamics.utils.routes.RoutesStart

@Composable
fun NavigationStart() {
    val navHost = rememberNavController()
    NavHost(
        navController = navHost,
        startDestination = RoutesStart.Login.route
    ) {
        composable(RoutesStart.Home.route) {
            HomeScreen()
        }
    }
}