package com.override.data.utils.data

import com.override.data.utils.enum.AttendanceStatus

// --- StudentWithStatus.kt (Nuevo - Helper para la UI) ---
// Combina el estudiante con su estado para una fecha específica (usado en la UI)
data class StudentWithStatus(
    val student: Student,
    val status: AttendanceStatus
)
