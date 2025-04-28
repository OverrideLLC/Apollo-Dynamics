package com.shared.ui

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

@Composable
fun onBackground(): Color {
    return lerp(colorScheme.onBackground, colorScheme.primary, 0.1f)
}