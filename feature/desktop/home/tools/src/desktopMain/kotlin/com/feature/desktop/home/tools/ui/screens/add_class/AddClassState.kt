package com.feature.desktop.home.tools.ui.screens.add_class

import androidx.compose.ui.graphics.Color

data class AddClassState(
    /**
     * Nombre de la clase que se va a agregar.
     */
    val className: String = "",
    /**
     * Grado al que pertenece la clase.
     */
    val degree: String = "",
    /**
     * Carrera a la que pertenece la clase.
     */
    val career: String = "",
    /**
     * Sección a la que pertenece la clase.
     */
    val section: String = "",
    /**
     * Color seleccionado para la clase. Por defecto es Gris.
     */
    val selectedColor: Color = Color.Gray,
    /**
     * Lista de colores disponibles para elegir como color de la clase.
     */
    val availableColors: List<Color> = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFFFC107),
        Color(0xFFFF9800), Color(0xFF795548), Color(0xFF607D8B)
    ),
    val isLoading: Boolean = false,
    /**
     * Mensaje de error si ocurre algún problema durante el proceso.
     */
    val error: String? = null,
    /**
     * Indica si la clase se guardó correctamente.
     */
    val saveSuccess: Boolean = false
)
