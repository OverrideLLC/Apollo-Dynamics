package com.feature.desktop.home.services.classroom.utils.enums

import com.shared.resources.Res
import com.shared.resources.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource

enum class ClassroomServices(
    val icon: DrawableResource,
    val title: String,
    val route: String
) {
    ADD_ANNOUNCE(
        icon = Res.drawable.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
        title = "Add Announcement",
        route = "add_announcement"
    )
}