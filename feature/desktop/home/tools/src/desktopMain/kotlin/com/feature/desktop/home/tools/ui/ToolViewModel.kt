package com.feature.desktop.home.tools.ui

import androidx.lifecycle.ViewModel
import com.feature.desktop.home.tools.utils.enum.Tools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.enums.EnumEntries

class ToolViewModel : ViewModel() {
    data class ToolState(
        val tools: EnumEntries<Tools> = Tools.entries,
        val toolSelection: Tools? = null,
    )

    private val _state = MutableStateFlow(ToolState())
    val state = _state.asStateFlow()

    fun toolSelection(tool: Tools?) {
        _state.value = _state.value.copy(
            toolSelection = tool,
        )
    }
}