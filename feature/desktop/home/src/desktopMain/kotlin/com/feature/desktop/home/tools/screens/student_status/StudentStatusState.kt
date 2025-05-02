package com.feature.desktop.home.tools.screens.student_status

import com.override.data.utils.data.ClassData
import com.override.data.utils.data.Student
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate

// Estado para la pantalla de estado del alumno
data class StudentStatusState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val student: Student? = null,
    val assignedClass: ClassData? = null, // La clase actual del alumno
    val attendanceHistory: List<AttendanceRecordItem> = emptyList()
)

// Data class simplificada para mostrar en la lista de historial
data class AttendanceRecordItem(
    val date: LocalDate,
    val status: AttendanceStatus,
    val className: String // Nombre de la clase para ese registro
)
