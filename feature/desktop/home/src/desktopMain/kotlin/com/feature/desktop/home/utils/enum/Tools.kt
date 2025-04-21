package com.feature.desktop.home.utils.enum

import com.feature.desktop.home.utils.routes.ToolRoute
import com.shared.resources.Res
import com.shared.resources.fact_check_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource

enum class Tools(
    val icon: DrawableResource,
    val nameString: String,
    val route: String,
) {
    TAKE_ATTENDEES(
        icon = Res.drawable.fact_check_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
        route = ToolRoute.TakeAttendees.route,
        nameString = "Take Attendees"
    )
}