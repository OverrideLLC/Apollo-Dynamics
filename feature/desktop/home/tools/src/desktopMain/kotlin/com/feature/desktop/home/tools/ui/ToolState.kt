package com.feature.desktop.home.tools.ui

import com.feature.desktop.home.tools.utils.enum.Tools
import kotlin.enums.EnumEntries

data class ToolState(
    val tools: EnumEntries<Tools> = Tools.entries,
    val toolSelection: Tools? = null
)