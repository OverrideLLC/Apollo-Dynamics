package com.feature.desktop.home.screen

import androidx.compose.runtime.*
import com.feature.desktop.home.utils.Services

@Immutable
data class HomeState(
    val isLoading: Boolean = false,
    val dockToLeft: Boolean = true,
    val dockToRight: Boolean = true,
    val serviceSelected: Services? = null,
)