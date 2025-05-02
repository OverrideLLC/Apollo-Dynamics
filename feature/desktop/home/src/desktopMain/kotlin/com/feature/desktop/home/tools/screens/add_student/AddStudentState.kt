package com.feature.desktop.home.tools.screens.add_student

import com.override.data.utils.data.ClassData

data class AddStudentState(
    val studentName: String = "",
    val studentId: String = "", // Opcional, dependiendo si necesitas un ID específico
    val studentEmail: String = "", // Opcional
    val studentNumber: String = "", // Opcional
    val studentControlNumber: String = "", // Opcional
    val selectedClass: ClassData? = null, // Clase seleccionada a la que se añadirá el estudiante
    val availableClasses: List<ClassData> = emptyList(), // Lista de clases disponibles
    val isLoadingClasses: Boolean = false, // Para indicar si se están cargando las clases
    val isLoadingSave: Boolean = false, // Para indicar si se está guardando el estudiante
    val error: String? = null,
    val saveSuccess: Boolean = false // Para indicar si se guardó correctamente
)
