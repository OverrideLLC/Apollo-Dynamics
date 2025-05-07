package com.feature.desktop.home.services.classroom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shared.utils.routes.RoutesServicesClassroom

@Composable
fun NavigationServicesClassroom(
    route: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = route,
    ) {
        composable(RoutesServicesClassroom.Classroom.route) {
        }
        composable(RoutesServicesClassroom.AddAnnouncement.route) {
        }
    }
}