package com.feature.desktop.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.desktop.home.utils.Services
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

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