package com.feature.desktop.home.utils.data

import com.feature.desktop.home.utils.enum.AttendanceStatus

data class Student(
    val id: String, // Identificador único del estudiante
    val name: String, // Nombre del estudiante
    val email: String, // Email (si es necesario)
    val number: String, // Número (si es necesario)
    val controlNumber: Int // Número de control
)