package com.feature.desktop.home.tools.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.tools.ui.screens.add_class.AddClassScreen
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentScreen
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesScreen
import com.feature.desktop.home.tools.utils.routes.ToolRoute

@Composable
fun NavigationTools(
    route: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = route
    ) {
        composable(route = ToolRoute.TakeAttendees.route) {
            TakeAttendeesScreen()
        }
        composable(route = ToolRoute.AddClass.route) {
            AddClassScreen(onCompletion = {})
        }
        composable(route = ToolRoute.AddStudent.route) {
            AddStudentScreen(onCompletion = {})
        }
    }
}