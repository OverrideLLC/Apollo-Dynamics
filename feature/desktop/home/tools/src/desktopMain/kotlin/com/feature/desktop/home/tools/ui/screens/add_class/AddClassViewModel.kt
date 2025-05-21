package com.feature.desktop.home.tools.ui.screens.add_class

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.network.interfaces.FirebaseGitliveRepository
import com.override.data.repository.contract.ClassRepository
import com.override.data.utils.data.ClassData
import com.shared.utils.data.firebase.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.io.Closeable
import java.util.UUID

/**
 * [AddClassViewModel] es el ViewModel responsable de gestionar la lógica de la pantalla de agregar una clase.
 * Interactúa con [ClassRepository] para realizar operaciones de guardado de clases.
 * @param classRepository Repositorio de clase utilizado para interactuar con la capa de datos.
 */
class AddClassViewModel(
    private val classRepository: ClassRepository,
    private val firebaseGitliveRepository: FirebaseGitliveRepository
) : ViewModel(), Closeable {

    /**
     * [customViewModelScope] es un [CoroutineScope] personalizado que se utiliza para las operaciones asíncronas
     * dentro de este ViewModel. Está vinculado a [Dispatchers.Swing] para actualizar la UI en el hilo principal de Swing.
     */
    private val customViewModelScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Swing)

    /**
     * [_state] es un [MutableStateFlow] que almacena el estado actual de la pantalla de agregar clase.
     */
    private val _state = MutableStateFlow(AddClassState())

    /**
     * [state] es un [StateFlow] expuesto al exterior que permite observar el estado de la pantalla de agregar clase.
     */
    val state = _state.asStateFlow()

    /**
     * [onClassNameChange] actualiza el nombre de la clase en el estado.
     */
    fun onClassNameChange(newName: String) {
        _state.update { it.copy(className = newName, error = null, saveSuccess = false) }
    }

    /**
     * [onDegreeChange] actualiza el grado de la clase en el estado.
     */
    fun onDegreeChange(newDegree: String) {
        _state.update { it.copy(degree = newDegree, error = null, saveSuccess = false) }
    }

    /**
     * [onCareerChange] actualiza la carrera de la clase en el estado.
     */
    fun onCareerChange(newCareer: String) {
        _state.update { it.copy(career = newCareer, error = null, saveSuccess = false) }
    }

    fun onSectionChange(newSection: String) {
        _state.update { it.copy(section = newSection, error = null, saveSuccess = false) }
    }

    /**
     * [onColorSelected] actualiza el color seleccionado de la clase en el estado.
     */
    fun onColorSelected(newColor: Color) {
        _state.update { it.copy(selectedColor = newColor, error = null, saveSuccess = false) }
    }

    /**
     * [saveClass] guarda la clase actual en la base de datos.
     * Primero valida que todos los campos estén completos.
     * Luego crea un objeto [ClassData] y llama a [ClassRepository.addOrUpdateClass] para guardar la clase.
     */
    fun saveClass() {
        val currentState = _state.value
        if (currentState.className.isBlank() || currentState.career.isBlank() || currentState.degree.isBlank() || currentState.section.isBlank()) {
            _state.update { it.copy(error = "Please fill in all fields.") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null, saveSuccess = false) }

        customViewModelScope.launch {
            try {
                val newClass = ClassData(
                    id = UUID.randomUUID().toString(),
                    name = currentState.className.trim(),
                    roster = emptyList(),
                    color = currentState.selectedColor,
                    degree = currentState.degree.trim(),
                    career = currentState.career.trim(),
                    section = currentState.section.trim(),
                    attendanceHistory = emptyList()
                )
                firebaseGitliveRepository.addCourse(
                    newCourse = Course(
                        id = newClass.id,
                        name = newClass.name,
                        degree = newClass.degree,
                        section = newClass.section,
                        career = newClass.career,
                    )
                )

                classRepository.addOrUpdateClass(newClass)
                _state.update {
                    AddClassState(saveSuccess = true)
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error saving class: ${e.message}"
                    )
                }
            } finally {
                if (!_state.value.saveSuccess) {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    /**
     * [close] cancela el [customViewModelScope] cuando el ViewModel se cierra.
     */
    override fun close() {
        customViewModelScope.cancel()
        println("AddClassViewModel scope cancelled")
    }

    /**
     * [onCleared] se llama cuando el ViewModel va a ser destruido. Se llama a [close] para cancelar el [customViewModelScope].
     */
    override fun onCleared() {
        super.onCleared()
        close()
    }
}