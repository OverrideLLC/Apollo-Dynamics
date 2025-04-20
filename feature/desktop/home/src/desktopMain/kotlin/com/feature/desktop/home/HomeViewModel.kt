package com.feature.desktop.home

import androidx.lifecycle.ViewModel
import com.shared.resources.Res
import com.shared.resources.*
import com.shared.utils.routes.RoutesHome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource

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