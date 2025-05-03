package com.feature.desktop.home.tools.screens.student_status

import androidx.lifecycle.ViewModel
import com.override.data.entity.AttendanceEntity
import com.override.data.repository.contract.AttendanceRepository
import com.override.data.repository.contract.ClassRepository
import com.override.data.repository.contract.StudentRepository
import com.override.data.utils.data.ClassData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.io.Closeable

// ViewModel para la pantalla de estado del alumno
class StudentStatusViewModel(
    private val studentRepository: StudentRepository,
    private val classRepository: ClassRepository,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel(), Closeable {
    private val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Swing) // Usar Default para lógica

    private val _state = MutableStateFlow(StudentStatusState())
    val state = _state.asStateFlow()

    fun loadStudentStatus(studentId: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                // 1. Obtener detalles del alumno
                val student = withContext(Dispatchers.Swing) {
                    studentRepository.getStudentById(studentId)
                }

                if (student == null) {
                    _state.update { it.copy(isLoading = false, error = "Student not found.") }
                    return@launch
                }

                // 2. Obtener la clase asignada (¡Requiere método en Repositorio/DAO!)
                // Esta es una implementación de EJEMPLO, necesitarás la real.
                val assignedClass = withContext(Dispatchers.Swing) {
                    findClassForStudent(studentId) // Función hipotética
                }

                // 3. Obtener historial de asistencia (¡Requiere método en DAO!)
                // Esta es una implementación de EJEMPLO.
                val attendanceEntities: List<AttendanceEntity> = withContext(Dispatchers.Swing) {
                    attendanceRepository.getAttendanceHistoryForStudent(
                        studentId = studentId,
                        classId = assignedClass?.id
                            ?: "" // Si no hay clase asignada, usar cadena vacía
                    )
                }

                // 4. Mapear historial a AttendanceRecordItem (obteniendo nombre de clase si es necesario)
                val attendanceHistoryItems = attendanceEntities.map { entity ->
                    // Si un alumno puede estar en varias clases, necesitarías buscar
                    // el nombre de la clase 'entity.classId' para cada registro.
                    // Por simplicidad, usaremos el nombre de la clase asignada actual si existe.
                    AttendanceRecordItem(
                        date = entity.date,
                        status = entity.status,
                        className = assignedClass?.name ?: "Unknown Class" // O buscar nombre real
                    )
                }

                // 5. Actualizar el estado final
                _state.update {
                    it.copy(
                        isLoading = false,
                        student = student,
                        assignedClass = assignedClass,
                        attendanceHistory = attendanceHistoryItems.sortedByDescending { item -> item.date } // Ordenar por fecha
                    )
                }

            } catch (e: Exception) {
                println("Error loading student status: ${e.message}")
                e.printStackTrace()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading student data: ${e.message}"
                    )
                }
            }
        }
    }

    // --- Función Hipotética (Necesitas implementarla en tu Repositorio/DAO) ---
    // Esta función buscaría en ClassStudentCrossRef o similar para encontrar la clase
    private suspend fun findClassForStudent(studentId: String): ClassData? {
        // Implementación de ejemplo (NO FUNCIONAL SIN DAO REAL):
        // val classId = classDao.findClassIdForStudent(studentId) // Método DAO hipotético
        // return if (classId != null) classRepository.getClassById(classId) else null

        // Alternativa simple (menos eficiente): buscar en todas las clases
        val allClasses =
            classRepository.getAllClasses().first() // Obtiene la lista actual de clases
        return allClasses.find { classData -> classData.roster.any { student -> student.id == studentId } }

        // return null // Placeholder
    }

    // --- Método Hipotético (Necesitas implementarlo en AttendanceDao) ---
    // Declaración de ejemplo para AttendanceDao:
    // @Query("SELECT * FROM attendance_records WHERE record_student_id = :studentId ORDER BY record_date DESC")
    // suspend fun getAttendanceHistoryForStudent(studentId: String): List<AttendanceEntity>


    override fun close() {
        viewModelScope.cancel()
        println("StudentStatusViewModel scope cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        close()
    }
}
