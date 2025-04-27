package com.feature.desktop.home.tools.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesScreen
import com.feature.desktop.home.utils.routes.ToolRoute

@Composable
fun NavigationTools(
    route: String
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = route
    ){
        composable(route = ToolRoute.TakeAttendees.route){
            TakeAttendeesScreen()
        }
    }
}