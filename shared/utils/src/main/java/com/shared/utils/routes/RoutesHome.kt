package com.shared.utils.routes

sealed class RoutesHome(val route: String) {
    data object Dashboard : RoutesHome("dashboard")
    data object Ai : RoutesHome("Ai")
    data object Settings : RoutesHome("settings")
    data object Profile : RoutesHome("profile")
    data object AddBusiness : RoutesHome("add_business")
}