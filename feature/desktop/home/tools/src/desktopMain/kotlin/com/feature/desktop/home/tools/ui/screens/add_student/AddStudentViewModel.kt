package com.feature.desktop.home.tools.ui.screens.add_student

import androidx.lifecycle.ViewModel
import com.override.data.repository.contract.ClassRepository
import com.override.data.repository.contract.StudentRepository
import com.override.data.utils.data.ClassData
import com.override.data.utils.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.io.Closeable
import java.util.UUID

// ViewModel para añadir estudiante
class AddStudentViewModel(
    private val classRepository: ClassRepository, // Para obtener clases y quizás actualizar roster
    private val studentRepository: StudentRepository
) : ViewModel(), Closeable {

    private val customViewModelScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Swing) // O Dispatchers.Default/IO

    private val _state = MutableStateFlow(AddStudentState())
    val state = _state.asStateFlow()

    init {
        loadAvailableClasses()
    }

    // Carga las clases disponibles para el selector
    private fun loadAvailableClasses() {
        _state.update { it.copy(isLoadingClasses = true, error = null) }
        try {
            print("Loading classes")
            classRepository.getAllClasses().onEach { classes ->
                _state.update { it.copy(availableClasses = classes, isLoadingClasses = false) }
                print(classes)
            }.launchIn(customViewModelScope) // Lanzar la colección en el scope del ViewModel
        } catch (e: Exception) {
            print("Error loading classes")
            _state.update {
                it.copy(
                    isLoadingClasses = false,
                    error = "Error loading classes: ${e.message}"
                )
            }
            print(e.message)
        }
    }

    // --- Funciones para actualizar el estado desde la UI ---

    fun onStudentNameChange(newName: String) {
        _state.update { it.copy(studentName = newName, error = null, saveSuccess = false) }
    }

    fun onStudentIdChange(newId: String) {
        _state.update { it.copy(studentId = newId, error = null, saveSuccess = false) }
    }

    fun onClassSelected(selectedClass: ClassData) {
        _state.update { it.copy(selectedClass = selectedClass, error = null, saveSuccess = false) }
    }

    fun onStudentEmailChange(newEmail: String) {
        _state.update { it.copy(studentEmail = newEmail, error = null, saveSuccess = false) }
    }

    fun onStudentNumberChange(newNumber: String) {
        _state.update { it.copy(studentNumber = newNumber, error = null, saveSuccess = false) }
    }

    fun onStudentControlNumberChange(newControlNumber: String) {
        _state.update { it.copy(studentControlNumber = newControlNumber, error = null, saveSuccess = false) }
    }

    // --- Función para guardar el estudiante ---

    fun saveStudent() {
        val currentState = _state.value

        // Validación
        if (currentState.studentName.isBlank()) {
            _state.update { it.copy(error = "Please enter the student's name.") }
            return
        }
        if (currentState.selectedClass == null) {
            _state.update { it.copy(error = "Please select a class.") }
            return
        }
        // Puedes añadir más validaciones (ej: formato de ID si lo usas)

        _state.update { it.copy(isLoadingSave = true, error = null, saveSuccess = false) }

        customViewModelScope.launch {
            try {
                // Crear el objeto Student
                val newStudent = Student(
                    id = UUID.randomUUID().toString(),
                    name = currentState.studentName.trim(),
                    email = currentState.studentEmail.trim(),
                    number = currentState.studentNumber.trim(),
                    controlNumber = currentState.studentControlNumber.toInt()
                )

                studentRepository.addOrUpdateStudent(newStudent)

                classRepository.addStudentToClass(
                    classId = currentState.selectedClass.id,
                    studentId = newStudent.id
                )

                // O si tienes un método específico en StudentRepository o ClassRepository:
                // studentRepository.addStudentToClass(newStudent, currentState.selectedClass.id)
                // classRepository.addStudentToClass(newStudent, currentState.selectedClass.id)

                // Actualizar estado para indicar éxito y limpiar formulario (o resetear)
                _state.update {
                    AddStudentState( // Resetea el estado manteniendo la lista de clases
                        availableClasses = it.availableClasses,
                        saveSuccess = true
                    )
                    // O solo limpiar campos específicos:
                    // it.copy(isLoadingSave = false, saveSuccess = true, studentName = "", studentId = "", selectedClass = null, error = null)
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingSave = false,
                        error = "Error saving student: ${e.message}"
                    )
                }
            } finally {
                // Asegurarse de quitar isLoadingSave si no se reseteó el estado completo
                if (!_state.value.saveSuccess) {
                    _state.update { it.copy(isLoadingSave = false) }
                }
            }
        }
    }

    override fun close() {
        customViewModelScope.cancel()
        println("AddStudentViewModel scope cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        close()
    }
}
