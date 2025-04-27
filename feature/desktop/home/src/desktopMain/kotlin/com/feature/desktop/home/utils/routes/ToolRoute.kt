package com.feature.desktop.home.utils.routes

sealed class ToolRoute(val route: String) {
    object TakeAttendees : ToolRoute("take_attendees")
}