package com.feature.desktop.home.tools.ui

import androidx.lifecycle.ViewModel
import com.feature.desktop.home.tools.utils.enum.Tools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ToolViewModel : ViewModel() {
    private val _state = MutableStateFlow(ToolState())
    val state = _state.asStateFlow()

    fun toolSelection(tool: Tools?) {
        _state.value = _state.value.copy(
            toolSelection = tool,
        )
    }
}