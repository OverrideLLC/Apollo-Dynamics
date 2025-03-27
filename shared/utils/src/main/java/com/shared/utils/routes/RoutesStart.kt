package com.shared.utils.routes

sealed class RoutesStart(val route: String) {
    data object Start : RoutesStart("Start")
    data object Home : RoutesStart("home")
}