package com.feature.desktop.home.utils

import com.shared.resources.Google_Drive_logo
import com.shared.resources.Res
import com.shared.resources.google_classroom
import com.shared.resources.moodle_logo
import com.shared.utils.routes.RouteServices
import org.jetbrains.compose.resources.DrawableResource

enum class Services(
    val nameServices: String,
    val description: String,
    val icon: DrawableResource,
    val route: RouteServices
) {
    ClassRoom(
        nameServices = "ClassRoom",
        description = "Open ClassRoom",
        icon = Res.drawable.google_classroom,
        route = RouteServices.ClassRoom
    ),
    Moodle(
        nameServices = "Moodle",
        description = "Open Moodle",
        icon = Res.drawable.moodle_logo,
        route = RouteServices.Moodle
    ),
    Drive(
        nameServices = "Drive",
        description = "Open Drive",
        icon = Res.drawable.Google_Drive_logo,
        route = RouteServices.Drive
    ),

}
