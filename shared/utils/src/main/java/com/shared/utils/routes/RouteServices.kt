package com.shared.utils.routes

sealed class RouteServices(val route: String) {
    object ClassRoom : RouteServices("ClassRoom")
    object Moodle : RouteServices("Moodle")
    object Drive : RouteServices("Drive")
}