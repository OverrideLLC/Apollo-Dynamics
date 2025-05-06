package com.feature.desktop.home.services.classroom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.services.classroom.screen.ClassroomScreen
import com.shared.utils.routes.RoutesServicesClassroom

@Composable
fun NavigationServices() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RoutesServicesClassroom.Classroom.route,
    ) {
        composable(RoutesServicesClassroom.Classroom.route) {
            ClassroomScreen()
        }
        composable(RoutesServicesClassroom.AddAnnouncement.route) {
        }
    }
}