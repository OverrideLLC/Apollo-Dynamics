package com.feature.desktop.home.utils

import com.shared.utils.routes.RouteServices
import org.jetbrains.compose.resources.DrawableResource

data class Services(
    val name: String,
    val description: String,
    val icon: DrawableResource,
    val route: RouteServices
)
