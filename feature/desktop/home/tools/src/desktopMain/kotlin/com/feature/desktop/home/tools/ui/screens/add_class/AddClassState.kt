package com.feature.desktop.home.tools.ui.screens.add_class

import androidx.compose.ui.graphics.Color

// Estado para la pantalla de añadir clase
data class AddClassState(
    val className: String = "",
    val degree: String = "",
    val career: String = "",
    val section: String = "",
    val selectedColor: Color = Color.Gray, // Color por defecto
    val availableColors: List<Color> = listOf( // Colores predefinidos para elegir
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFFFC107),
        Color(0xFFFF9800), Color(0xFF795548), Color(0xFF607D8B)
    ),
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false // Para indicar si se guardó correctamente
)
