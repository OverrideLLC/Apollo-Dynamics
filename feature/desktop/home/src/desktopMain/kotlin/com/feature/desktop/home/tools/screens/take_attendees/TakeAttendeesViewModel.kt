package com.feature.desktop.home.tools.screens.take_attendees


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.override.data.repository.contract.AttendanceRepository
import com.override.data.repository.contract.ClassRepository
import com.override.data.utils.data.ClassData
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import qrgenerator.qrkitpainter.QrKitBallShape
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitOptions
import qrgenerator.qrkitpainter.QrKitPixelShape
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPainter
import qrgenerator.qrkitpainter.createRoundCorners
import qrgenerator.qrkitpainter.solidBrush

// Quitar imports innecesarios como Random, DateTimeUnit, plus si ya no se usan para sample data

// *** PASO 1: Añadir Repositorios al Constructor ***
// Idealmente, Koin inyectaría estos.
class TakeAttendeesViewModel(
    private val classRepository: ClassRepository,
    private val attendanceRepository: AttendanceRepository,
    // private val studentRepository: StudentRepository // Añadir si se necesita directamente
) : ViewModel() {

    private val customViewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Swing)

    // El State se mantiene igual
    data class TakeAttendeesState(
        val isLoading: Boolean = true, // Empezar cargando
        val allClasses: List<ClassData> = emptyList(),
        val selectedClassId: String? = null,
        val availableDates: List<LocalDate> = emptyList(),
        val selectedDate: LocalDate? = null,
        val studentsForSelectedDate: List<StudentWithStatus> = emptyList(),
        val error: String? = null,
        val qr: QrPainter? = null,
        val tokenAttendees: String? = null,
        val newClass: Boolean? = null,
    )

    private val _state = MutableStateFlow(TakeAttendeesState())
    val state = _state.asStateFlow()

    // Reloj y Zona Horaria (se mantienen)
    private val clock: Clock = Clock.System
    private val zoneId: TimeZone = TimeZone.currentSystemDefault()


    init {
        // *** PASO 2: Cargar Datos desde Repositorios ***
        loadInitialDataFromDb()
    }

    private fun loadInitialDataFromDb() {
        _state.update { it.copy(isLoading = true, error = null) }

        // Observar el Flow de clases desde el repositorio
        classRepository.getAllClasses()
            .onEach { classes ->
                // Cuando la lista de clases cambie (desde la BD)...
                val currentSelectedId = _state.value.selectedClassId
                val selectedClassStillExists = classes.any { it.id == currentSelectedId }
                val newSelectedClassId =
                    if (selectedClassStillExists) currentSelectedId else classes.firstOrNull()?.id

                _state.update { it.copy(allClasses = classes) } // Actualizar la lista de clases

                if (newSelectedClassId != null) {
                    // Si hay una clase seleccionada (o se seleccionó la primera), cargar sus detalles
                    selectClassInternal(newSelectedClassId) // Usar función interna para evitar bucles
                } else {
                    // No hay clases, limpiar estado relacionado
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedClassId = null,
                            availableDates = emptyList(),
                            selectedDate = null,
                            studentsForSelectedDate = emptyList()
                        )
                    }
                }
            }
            .launchIn(customViewModelScope) // Lanzar la colección en el scope del ViewModel
    }

    // --- Acciones del Usuario (Modificadas para usar Repositorios) ---

    fun selectClass(classId: String) {
        selectClassInternal(classId)
    }

    // Función interna para ser llamada desde la carga inicial o la acción del usuario
    private fun selectClassInternal(classId: String) {
        _state.update {
            it.copy(
                isLoading = true,
                selectedClassId = classId,
                error = null
            )
        } // Marcar carga y seleccionar ID

        viewModelScope.launch(Dispatchers.Swing) {
            try {
                // Obtener las fechas disponibles para esta clase desde el repositorio
                val availableDates = classRepository.getAttendanceDatesForClass(classId)
                val dateToSelect =
                    availableDates.firstOrNull() // Seleccionar la más reciente por defecto

                _state.update {
                    it.copy(
                        isLoading = false,
                        availableDates = availableDates,
                        selectedDate = dateToSelect,
                        studentsForSelectedDate = emptyList()
                    )
                }

                // Si se seleccionó una fecha, cargar los estudiantes para esa fecha
                if (dateToSelect != null) {
                    selectDate(dateToSelect) // Llama a la función selectDate para cargar estudiantes
                } else {
                    // Si no hay fechas, indicar que no hay carga pendiente
                    _state.update { it.copy(isLoading = false) }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading class dates: ${e.message}"
                    )
                }
            }
        }
    }


    fun selectDate(date: LocalDate) {
        val classId = _state.value.selectedClassId ?: return
        _state.update {
            it.copy(
                isLoading = true,
                selectedDate = date,
                error = null
            )
        } // Marcar carga y seleccionar fecha

        viewModelScope.launch(Dispatchers.Swing) {
            try {
                // Obtener estudiantes con estado para la clase y fecha seleccionadas desde el repositorio
                val students = attendanceRepository.getAttendanceForClassOnDate(classId, date)
                _state.update {
                    it.copy(
                        isLoading = false,
                        studentsForSelectedDate = students
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading students for date: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateStudentAttendanceStatus(studentId: String, newStatus: AttendanceStatus) {
        val classId = _state.value.selectedClassId ?: return
        val date = _state.value.selectedDate ?: return
        // No necesitamos actualizar el estado localmente aquí.
        // El cambio se hará en la BD y el Flow lo reflejará.

        viewModelScope.launch(Dispatchers.Swing) {
            _state.update { it.copy(isLoading = true) } // Opcional: indicar carga breve
            try {
                // Llamar al repositorio para actualizar la BD
                attendanceRepository.updateStudentAttendanceStatus(
                    classId,
                    studentId,
                    date,
                    newStatus
                )
                // ¡Importante! No actualizamos `studentsForSelectedDate` directamente aquí.
                // La actualización vendrá automáticamente cuando `getAttendanceForClassOnDate`
                // sea llamado de nuevo (implícitamente si `selectDate` se llama de nuevo,
                // o explícitamente si forzamos una recarga).
                // Para una UI más reactiva, podríamos forzar la recarga de la fecha actual:
                selectDate(date) // Vuelve a cargar los datos para la fecha actual
                // _state.update { it.copy(isLoading = false) } // isLoading se maneja en selectDate

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error updating status: ${e.message}"
                    )
                }
            }
        }
    }

    fun addNewAttendanceDay(targetDate: LocalDate = clock.todayIn(zoneId)) {
        val classId = _state.value.selectedClassId ?: return
        val selectedClass = _state.value.allClasses.find { it.id == classId } ?: return

        // Verificar si ya existe localmente (podría chequearse en repo también)
        if (_state.value.availableDates.contains(targetDate)) {
            println("Attendance record for $targetDate already exists.")
            if (_state.value.selectedDate != targetDate) {
                selectDate(targetDate) // Selecciona la fecha existente
            }
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Swing) {
            try {
                // 1. Obtener el roster actual (necesario para inicializar el día)
                //    Asumiendo que classRepository.getClassById devuelve ClassData con roster
                val classData = classRepository.getClassById(classId)
                if (classData == null) {
                    _state.update { it.copy(isLoading = false, error = "Class not found") }
                    return@launch
                }

                // 2. Crear el mapa inicial (todos UNKNOWN o PRESENT por defecto?)
                val initialAttendanceMap = classData.roster.associate { student ->
                    student.id to AttendanceStatus.UNKNOWN // O AttendanceStatus.PRESENT si prefieres
                }

                // 3. Llamar al repositorio para guardar el nuevo registro de asistencia
                attendanceRepository.recordAttendance(classId, targetDate, initialAttendanceMap)

                // 4. Refrescar datos para la clase seleccionada para obtener la nueva lista de fechas
                //    y seleccionar automáticamente el nuevo día.
                selectClassInternal(classId) // Esto recargará las fechas y seleccionará la más reciente (que será la nueva)

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error adding new attendance day: ${e.message}"
                    )
                }
            }
        }
    }


    // --- Funciones de QR y Añadir Clase (requieren lógica adicional) ---

    fun generateAttendanceQr() {
        // La lógica del token y QR se mantiene igual por ahora
        _state.update {
            it.copy(
                qr = qr(it.tokenAttendees ?: "Non")
            )
        }
    }

    private fun qr(
        token: String,
    ): QrPainter {
        // ... (código del QR sin cambios)
        return QrPainter(
            content = token,
            config = QrKitOptions(
                shapes = QrKitShapes(
                    darkPixelShape = QrKitPixelShape.createRoundCorners(),
                    ballShape = QrKitBallShape.createRoundCorners(1f)
                ),
                colors = QrKitColors(
                    lightBrush = QrKitBrush.solidBrush(color = Color.Transparent),
                    ballBrush = QrKitBrush.solidBrush(
                        color = Color(0xff000000)
                    ),
                    frameBrush = QrKitBrush.solidBrush(
                        color = Color(0xff000000)
                    ),
                    darkBrush = QrKitBrush.solidBrush(
                        color = Color(0xff000000)
                    )
                )
            ),
        )
    }

    fun addNewClass() {
        _state.update { it.copy(newClass = true) }
    }

    fun closeQr() {
        _state.update { it.copy(qr = null) }
    }

    fun closeNewClass() {
        _state.update { it.copy(newClass = null) }
    }
}