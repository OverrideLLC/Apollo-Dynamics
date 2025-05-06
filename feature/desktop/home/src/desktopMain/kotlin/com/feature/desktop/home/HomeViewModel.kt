package com.feature.desktop.home

import androidx.lifecycle.ViewModel
import com.feature.desktop.home.utils.Services
import com.shared.resources.Res
import com.shared.resources.*
import com.shared.utils.routes.RouteServices
import com.shared.utils.routes.RoutesHome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.DrawableResource

class HomeViewModel : ViewModel() {
    data class HomeState(
        val dockToLeft: Boolean = false,
        val dockToRight: Boolean = false,
        val serviceSelected: Services? = null,
    )

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun serviceSelected(service: Services?) {
        _state.update {
            it.copy(
                serviceSelected = service
            )
        }
    }

    fun dockToLeft() {
        _state.update {
            it.copy(
                dockToLeft = !_state.value.dockToLeft
            )
        }
    }

    fun dockToRight() {
        _state.update {
            it.copy(
                dockToRight = !_state.value.dockToRight
            )
        }
    }
}