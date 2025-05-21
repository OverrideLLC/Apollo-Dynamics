package com.shared.utils.routes

sealed class RoutesHome(val route: String) {
    data object Dashboard : RoutesHome("dashboard")
    data object Services : RoutesHome("Services")
}