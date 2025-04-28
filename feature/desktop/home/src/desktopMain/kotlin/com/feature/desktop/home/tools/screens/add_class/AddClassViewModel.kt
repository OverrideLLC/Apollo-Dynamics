package com.feature.desktop.home.tools.screens.add_class

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.override.data.repository.contract.ClassRepository
import com.override.data.utils.data.ClassData
import com.override.data.utils.data.Student
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

// ViewModel
class AddClassViewModel(
    private val classRepository: ClassRepository // Inyectar repositorio
) : ViewModel(), Closeable {

    private val customViewModelScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Swing)

    private val _state = MutableStateFlow(AddClassState())
    val state = _state.asStateFlow()

    // --- Funciones para actualizar el estado desde la UI ---

    fun onClassNameChange(newName: String) {
        _state.update { it.copy(className = newName, error = null, saveSuccess = false) }
    }

    fun onDegreeChange(newDegree: String) {
        _state.update { it.copy(degree = newDegree, error = null, saveSuccess = false) }
    }

    fun onCareerChange(newCareer: String) {
        _state.update { it.copy(career = newCareer, error = null, saveSuccess = false) }
    }

    fun onSectionChange(newSection: String) {
        _state.update { it.copy(section = newSection, error = null, saveSuccess = false) }
    }

    fun onColorSelected(newColor: Color) {
        _state.update { it.copy(selectedColor = newColor, error = null, saveSuccess = false) }
    }

    // --- Función para guardar la clase ---

    fun saveClass() {
        val currentState = _state.value
        // Validación simple (puedes añadir más)
        if (currentState.className.isBlank() || currentState.career.isBlank() || currentState.degree.isBlank() || currentState.section.isBlank()) {
            _state.update { it.copy(error = "Please fill in all fields.") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null, saveSuccess = false) }

        customViewModelScope.launch {
            try {
                // Crear el objeto ClassData
                // Nota: El roster inicial está vacío. Se añadirán estudiantes después.
                val newClass = ClassData(
                    id = UUID.randomUUID().toString(), // Generar un ID único
                    name = currentState.className.trim(),
                    roster = emptyList<Student>(), // Roster inicial vacío
                    color = currentState.selectedColor,
                    degree = currentState.degree.trim(),
                    career = currentState.career.trim(),
                    section = currentState.section.trim(),
                    attendanceHistory = emptyList() // Historial vacío inicialmente
                )

                // Llamar al repositorio para guardar
                classRepository.addOrUpdateClass(newClass)

                // Actualizar estado para indicar éxito y limpiar formulario
                _state.update {
                    AddClassState(saveSuccess = true) // Resetear el estado excepto el éxito
                    // O podrías solo limpiar los campos:
                    // it.copy(isLoading = false, saveSuccess = true, className = "", degree = "", career = "", section = "")
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error saving class: ${e.message}"
                    )
                }
            } finally {
                // Asegurarse de quitar isLoading incluso si hay éxito (si no se resetea el estado)
                if (!_state.value.saveSuccess) { // Si no se reseteó el estado completo
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    override fun close() {
        customViewModelScope.cancel()
        println("AddClassViewModel scope cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        close()
    }
}