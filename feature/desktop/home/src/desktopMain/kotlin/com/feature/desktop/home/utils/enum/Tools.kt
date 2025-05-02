package com.feature.desktop.home.utils.enum

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.utils.routes.ToolRoute
import com.shared.resources.Res
import com.shared.resources.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.fact_check_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.person_add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource

enum class Tools(
    val icon: DrawableResource,
    val nameString: String,
    val route: String,
    val size: DpSize = DpSize(width = 400.dp, height = 600.dp)
) {
    TAKE_ATTENDEES(
        icon = Res.drawable.fact_check_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
        route = ToolRoute.TakeAttendees.route,
        nameString = "Take Attendees",
        size = DpSize(width = 1200.dp, height = 720.dp)
    ),
    ADD_CLASS(
        icon = Res.drawable.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
        route = ToolRoute.AddClass.route,
        nameString = "Add Class",
        size = DpSize(width = 1200.dp, height = 720.dp)
    ),
    ADD_STUDENT(
        icon = Res.drawable.person_add_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
        route = ToolRoute.AddStudent.route,
        nameString = "Add Student",
        size = DpSize(width = 600.dp, height = 850.dp)

    )
}