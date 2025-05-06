package com.override.data.utils.data

import androidx.compose.ui.graphics.Color
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate


data class ClassData(
    val id: String,
    val name: String,
    val roster: List<Student>, // La lista de estudiantes inscritos (roster)
    val color: Color,
    val degree: String,
    val career: String,
    val section: String,
    val attendanceHistory: List<AttendanceRecord> = emptyList()
) {
    // Helper para obtener la lista de estudiantes con su estado para una fecha específica
    // Esto es útil para la UI, para no tener que pasar el mapa completo
    fun getStudentsWithStatusForDate(targetDate: LocalDate): List<StudentWithStatus> {
        val record = attendanceHistory.find { it.date == targetDate }
        return roster.map { student ->
            StudentWithStatus(
                student = student,
                // Obtiene el estado del record del día, o UNKNOWN si no existe el record o el estudiante en el mapa
                status = record?.attendance?.get(student.id) ?: AttendanceStatus.UNKNOWN
            )
        }
    }

    // Helper para obtener solo las fechas con registros
    fun getAttendanceDates(): List<LocalDate> {
        return attendanceHistory.map { it.date }.sortedDescending()
    }
}