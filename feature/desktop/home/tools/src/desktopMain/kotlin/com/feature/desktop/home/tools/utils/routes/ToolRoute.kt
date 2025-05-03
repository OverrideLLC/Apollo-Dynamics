package com.feature.desktop.home.tools.utils.routes

sealed class ToolRoute(val route: String) {
    object TakeAttendees : ToolRoute("take_attendees")
    object AddClass : ToolRoute("add_class")
    object AddStudent : ToolRoute("add_student")
    object AddTeacher : ToolRoute("add_teacher")
}