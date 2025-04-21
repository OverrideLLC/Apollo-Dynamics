package com.feature.desktop.home.tools

import androidx.lifecycle.ViewModel
import com.feature.desktop.home.utils.enum.Tools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.enums.EnumEntries

class ToolViewModel : ViewModel() {
    data class ToolState(
        val tools: EnumEntries<Tools> = Tools.entries
    )

    private val _state = MutableStateFlow(ToolState())
    val state = _state.asStateFlow()

}