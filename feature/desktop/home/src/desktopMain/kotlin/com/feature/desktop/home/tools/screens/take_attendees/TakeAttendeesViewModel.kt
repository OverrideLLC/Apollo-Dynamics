package com.feature.desktop.home.tools.screens.take_attendees

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.desktop.home.utils.data.* // Importar todos los modelos de datos
import com.feature.desktop.home.utils.enum.AttendanceStatus
import kotlinx.coroutines.flow.*
import kotlinx.datetime.* // Importar kotlinx-datetime
import kotlin.random.Random

class TakeAttendeesViewModel : ViewModel() {
    data class TakeAttendeesState(
        val isLoading: Boolean = false,
        val allClasses: List<ClassData> = emptyList(), // Todas las clases disponibles
        val selectedClassId: String? = null, // ID de la clase seleccionada
        val availableDates: List<LocalDate> = emptyList(), // Fechas con asistencia para la clase seleccionada
        val selectedDate: LocalDate? = null, // Fecha seleccionada para mostrar/editar
        // Lista de estudiantes CON su estado para la clase y fecha seleccionadas
        val studentsForSelectedDate: List<StudentWithStatus> = emptyList(),
        val error: String? = null
    )

    private val _state = MutableStateFlow(TakeAttendeesState())
    val state = _state.asStateFlow()

    // Reloj para obtener la fecha actual (importante para multiplatform)
    private val clock: Clock = Clock.System
    // Zona horaria del sistema (puede necesitar ajuste para diferentes plataformas)
    private val zoneId: TimeZone = TimeZone.currentSystemDefault()


    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _state.update { it.copy(isLoading = true) }
        // Simular carga de datos (reemplazar con tu lógica real: Koin, Firebase, etc.)
        val sampleClasses = generateSampleClassDataWithHistory(5)
        val initialClass = sampleClasses.firstOrNull()
        val initialDate = initialClass?.getAttendanceDates()?.firstOrNull() // La fecha más reciente

        _state.update {
            it.copy(
                isLoading = false,
                allClasses = sampleClasses,
                selectedClassId = initialClass?.id,
                availableDates = initialClass?.getAttendanceDates() ?: emptyList(),
                selectedDate = initialDate,
                studentsForSelectedDate = initialClass?.getStudentsWithStatusForDate(initialDate ?: clock.todayIn(zoneId)) ?: emptyList()
            )
        }
    }

    fun selectClass(classId: String) {
        val selectedClass = _state.value.allClasses.find { it.id == classId } ?: return
        val availableDates = selectedClass.getAttendanceDates()
        val dateToSelect = availableDates.firstOrNull() // Seleccionar la más reciente por defecto

        _state.update {
            it.copy(
                selectedClassId = classId,
                availableDates = availableDates,
                selectedDate = dateToSelect,
                studentsForSelectedDate = selectedClass.getStudentsWithStatusForDate(dateToSelect ?: clock.todayIn(zoneId)) // Cargar estudiantes para esa fecha
            )
        }
        println("Selected Class: ${selectedClass.name}, Dates: $availableDates, Selected Date: $dateToSelect")
    }

    fun selectDate(date: LocalDate) {
        val selectedClass = _state.value.allClasses.find { it.id == _state.value.selectedClassId } ?: return

        _state.update {
            it.copy(
                selectedDate = date,
                studentsForSelectedDate = selectedClass.getStudentsWithStatusForDate(date)
            )
        }
        println("Selected Date: $date")
    }

    fun updateStudentAttendanceStatus(studentId: String, newStatus: AttendanceStatus) {
        val currentState = _state.value
        val classId = currentState.selectedClassId ?: return
        val date = currentState.selectedDate ?: return

        _state.update {
            // Crear nuevas listas/mapas para asegurar la actualización del estado
            val updatedClasses = it.allClasses.map { classItem ->
                if (classItem.id == classId) {
                    // Encontrar o crear el registro para la fecha
                    val existingRecordIndex = classItem.attendanceHistory.indexOfFirst { record -> record.date == date }
                    val updatedHistory: List<AttendanceRecord>

                    if (existingRecordIndex != -1) {
                        // Modificar registro existente
                        val oldRecord = classItem.attendanceHistory[existingRecordIndex]
                        val updatedAttendanceMap = oldRecord.attendance.toMutableMap()
                        updatedAttendanceMap[studentId] = newStatus
                        val newRecord = oldRecord.copy(attendance = updatedAttendanceMap)
                        updatedHistory = classItem.attendanceHistory.toMutableList().apply {
                            set(existingRecordIndex, newRecord)
                        }
                    } else {
                        // Esto no debería pasar si se creó el día primero, pero como fallback:
                        println("Warning: Updating status for a date with no existing record. Creating one.")
                        val newAttendanceMap = mapOf(studentId to newStatus)
                        // Asegurarse que todos los demás estudiantes tengan UNKNOWN
                        val completeAttendanceMap = classItem.roster.associate { student ->
                            student.id to (newAttendanceMap[student.id] ?: AttendanceStatus.UNKNOWN)
                        }
                        val newRecord = AttendanceRecord(date = date, attendance = completeAttendanceMap)
                        updatedHistory = classItem.attendanceHistory + newRecord
                    }
                    classItem.copy(attendanceHistory = updatedHistory.sortedByDescending { r -> r.date }) // Mantener ordenado
                } else {
                    classItem // Devolver otras clases sin modificar
                }
            }

            // Recalcular la lista de estudiantes para la UI con el estado actualizado
            val updatedSelectedClass = updatedClasses.find { c -> c.id == classId }
            val updatedStudentsForDate = updatedSelectedClass?.getStudentsWithStatusForDate(date) ?: emptyList()

            it.copy(
                allClasses = updatedClasses,
                studentsForSelectedDate = updatedStudentsForDate
                // availableDates podría necesitar actualizarse si se creó un nuevo registro (ver addNewAttendanceDay)
            )
        }
        println("Updated status for $studentId to $newStatus on $date")
    }

    fun addNewAttendanceDay(targetDate: LocalDate = clock.todayIn(zoneId)) {
        val currentState = _state.value
        val classId = currentState.selectedClassId ?: return
        val selectedClass = currentState.allClasses.find { it.id == classId } ?: return

        // Verificar si ya existe un registro para este día
        if (selectedClass.attendanceHistory.any { it.date == targetDate }) {
            println("Attendance record for $targetDate already exists.")
            // Opcionalmente, seleccionar esa fecha si no es la actual
            if (currentState.selectedDate != targetDate) {
                selectDate(targetDate)
            }
            return
        }

        _state.update { it.copy(isLoading = true) } // Indicar carga

        // Crear el nuevo registro con todos los estudiantes como UNKNOWN
        val initialAttendanceMap = selectedClass.roster.associate { student ->
            student.id to AttendanceStatus.UNKNOWN
        }
        val newRecord = AttendanceRecord(date = targetDate, attendance = initialAttendanceMap)

        // Actualizar la lista de clases
        _state.update {
            val updatedClasses = it.allClasses.map { classItem ->
                if (classItem.id == classId) {
                    val updatedHistory = (classItem.attendanceHistory + newRecord).sortedByDescending { r -> r.date }
                    classItem.copy(attendanceHistory = updatedHistory)
                } else {
                    classItem
                }
            }
            val newSelectedClass = updatedClasses.find { c -> c.id == classId }!!
            val newAvailableDates = newSelectedClass.getAttendanceDates()

            it.copy(
                isLoading = false,
                allClasses = updatedClasses,
                availableDates = newAvailableDates,
                selectedDate = targetDate, // Seleccionar automáticamente el nuevo día
                studentsForSelectedDate = newSelectedClass.getStudentsWithStatusForDate(targetDate) // Mostrar la lista del nuevo día
            )
        }
        println("Added new attendance day: $targetDate for class $classId")
    }
}


// --- Función de Ejemplo Actualizada ---
private fun generateSampleClassDataWithHistory(count: Int): List<ClassData> {
    val random = Random(System.currentTimeMillis())
    val clock: Clock = Clock.System
    val zoneId: TimeZone = TimeZone.currentSystemDefault()
    val today = clock.todayIn(zoneId)

    return List(count) { classIndex ->
        val roster = List(random.nextInt(15, 25)) { studentIndex ->
            Student(
                id = "student_${classIndex}_$studentIndex",
                name = "Student ${('A'..'Z').random()}${('a'..'z').random()}${('a'..'z').random()} ${('A'..'Z').random()}.",
                email = "student${classIndex}_${studentIndex}@example.com",
                number = "N${random.nextInt(1000, 9999)}",
                controlNumber = 1000 + studentIndex
            )
        }.sortedBy { it.name }

        // Crear historial de asistencia para los últimos 3 días (ejemplo)
        val history = (-2..0).map { dayOffset ->
            val date = today.plus(dayOffset, DateTimeUnit.DAY)
            val attendanceMap = roster.associate { student ->
                // Asignar estado aleatorio (excepto UNKNOWN) para días pasados
                val status = if (dayOffset < 0) AttendanceStatus.entries.filter { it != AttendanceStatus.UNKNOWN }.random(random) else AttendanceStatus.UNKNOWN
                student.id to status
            }
            AttendanceRecord(date = date, attendance = attendanceMap)
        }

        ClassData(
            id = "class_$classIndex",
            name = "Class ${classIndex + 1}",
            degree = "Grade ${random.nextInt(1, 6)}",
            career = if (classIndex % 2 == 0) "Systems Eng." else "Computer Sci.",
            section = "Section ${('A'..'C').random()}",
            roster = roster, // Guardar la lista base de estudiantes
            color = Color(
                red = random.nextFloat() * 0.6f + 0.2f,
                green = random.nextFloat() * 0.6f + 0.2f,
                blue = random.nextFloat() * 0.6f + 0.2f
            ),
            attendanceHistory = history // Asignar el historial generado
        )
    }
}