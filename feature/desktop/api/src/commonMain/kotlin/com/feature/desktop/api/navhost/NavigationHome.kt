package com.feature.desktop.api.navhost

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shared.utils.routes.RoutesHome

@Composable
fun NavigationHome(navHost: NavHostController) {
    NavHost(
        navController = navHost,
        startDestination = RoutesHome.Dashboard.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -it })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it })
        },
    ) {
        composable(RoutesHome.Dashboard.route) {
            //DashboardScreen()
        }
        composable(RoutesHome.Ai.route) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "AI",
                    color = colorScheme.tertiary,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        composable(RoutesHome.Settings.route) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "Settings",
                    color = colorScheme.tertiary,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        composable(RoutesHome.Profile.route) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "Profile",
                    color = colorScheme.tertiary,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        composable(RoutesHome.AddBusiness.route) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "Add Business",
                    color = colorScheme.tertiary,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}