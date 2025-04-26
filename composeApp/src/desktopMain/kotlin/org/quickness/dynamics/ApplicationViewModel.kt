package org.quickness.dynamics

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.Window

class ApplicationViewModel: ViewModel() {
    @Immutable
    data class ApplicationState(
        val name: String = "TaskTec",
        val isOpened: Boolean = true,
        val windows: List<Window> = emptyList(),
        val darkTheme: Boolean = false
    )

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state.asStateFlow()

    fun update(update: ApplicationState.() -> ApplicationState) {
        _state.value = _state.value.update()
    }
}