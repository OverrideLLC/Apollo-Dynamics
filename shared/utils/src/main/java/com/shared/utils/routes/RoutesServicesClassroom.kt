package com.shared.utils.routes

sealed class RoutesServicesClassroom(val route: String) {
    object AddAnnouncement : RoutesServicesClassroom("add_announcement")
    object AddStudent : RoutesServicesClassroom("add_student")
    object StudentStatus : RoutesServicesClassroom("student_status")
    object Classroom : RoutesServicesClassroom("classroom")
}