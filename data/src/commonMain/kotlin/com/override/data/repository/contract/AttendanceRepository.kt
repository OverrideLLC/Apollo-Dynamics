package com.override.data.repository.contract

import com.override.data.entity.AttendanceEntity
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate

// --- Interfaz ---
interface AttendanceRepository {
    // Registra la asistencia para una clase completa en una fecha dada
    suspend fun recordAttendance(classId: String, date: LocalDate, attendanceMap: Map<String, AttendanceStatus>)

    // Obtiene la lista de estudiantes con su estado de asistencia para una clase y fecha específicas
    suspend fun getAttendanceForClassOnDate(classId: String, date: LocalDate): List<StudentWithStatus>

    suspend fun getAttendanceHistoryForStudent(studentId: String, classId: String): List<AttendanceEntity>

    // Actualiza el estado de asistencia de un solo estudiante en una fecha
    suspend fun updateStudentAttendanceStatus(classId: String, studentId: String, date: LocalDate, newStatus: AttendanceStatus)
}

