package com.feature.desktop.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.quickness.dynamics.utils.routes.RoutesHome
import quicknessdynamics.composeapp.generated.resources.Res
import quicknessdynamics.composeapp.generated.resources.add_business_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.chat_bubble_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.dashboard_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import quicknessdynamics.composeapp.generated.resources.settings_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24

class HomeViewModel : ViewModel() {
    data class HomeState(
        val icons: List<IconsBottomBar> = listOf(
            IconsBottomBar(
                RoutesHome.Settings.route,
                Res.drawable.settings_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            ),
            IconsBottomBar(
                RoutesHome.Ai.route,
                Res.drawable.chat_bubble_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            ),
            IconsBottomBar(
                RoutesHome.Dashboard.route,
                Res.drawable.dashboard_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            ),
            IconsBottomBar(
                RoutesHome.Profile.route,
                Res.drawable.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            ),
            IconsBottomBar(
                RoutesHome.AddBusiness.route,
                Res.drawable.add_business_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            )
        )
    )

    data class IconsBottomBar(
        val route: String,
        val icon: DrawableResource
    )

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
}