package com.feature.desktop.home.utils.data

import com.feature.desktop.home.utils.enum.AttendanceStatus

// --- StudentWithStatus.kt (Nuevo - Helper para la UI) ---
// Combina el estudiante con su estado para una fecha específica (usado en la UI)
data class StudentWithStatus(
    val student: Student,
    val status: AttendanceStatus
)
