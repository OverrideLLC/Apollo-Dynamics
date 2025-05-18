package com.feature.desktop.home.services.classroom.services.uploadassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.CourseWork // Importa la clase CourseWork de la API de Classroom
import com.network.repositories.contract.ClassroomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI de subida de tarea
// Data class to represent the state of the upload assignment UI
data class UploadAssignmentUiState(
    val isLoading: Boolean = false,
    val uploadSuccess: Boolean = false,
    val error: String? = null,
    val selectedCourse: Course? = null,
    val courses: List<Course>? = null
)

class UploadAssignmentViewModel(
    private val repositoryClassroom: ClassroomRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadAssignmentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCourses()
    }

    /**
     * Sube una nueva tarea a un curso.
     * Uploads a new assignment to a course.
     *
     * @param courseId El ID del curso donde se subirá la tarea.
     * @param assignmentDetails Un objeto [CourseWork] con los detalles de la tarea (título, descripción, puntos, etc.).
     */
    fun uploadAssignment(
        courseId: String,
        assignmentDetails: CourseWork
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, uploadSuccess = false, error = null) }
            runCatching {
                // Llama a la función del repositorio para crear el trabajo de clase
                // Call the repository function to create the coursework
                repositoryClassroom.createCourseWork(courseId, assignmentDetails)
            }.onSuccess { createdCourseWork ->
                if (createdCourseWork != null) {
                    _uiState.update { it.copy(isLoading = false, uploadSuccess = true) }
                    println("Tarea subida con éxito: ${createdCourseWork.title}")
                } else {
                    // Manejar el caso en que la API no devolvió el objeto creado (posible error sin excepción)
                    // Handle the case where the API did not return the created object (possible error without exception)
                    _uiState.update { it.copy(isLoading = false, uploadSuccess = false, error = "Error al subir la tarea: La API no devolvió el objeto creado.") }
                    println("Error al subir la tarea: La API no devolvió el objeto creado.")
                }
            }.onFailure { e ->
                println("Error al subir la tarea: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        uploadSuccess = false,
                        error = "Error al subir la tarea: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Restablece el estado de la UI de subida.
     * Resets the upload UI state.
     */
    fun resetState() {
        _uiState.update { UploadAssignmentUiState() }
    }

    fun getCourses() {
        _uiState.update { it.copy(isLoading = true, uploadSuccess = false, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repositoryClassroom.getAllCourses()
            }.onSuccess { courses ->
                _uiState.update { it.copy(isLoading = false, courses = courses) }
            }.onFailure { e ->
                println("Error al obtener cursos: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        uploadSuccess = false,
                        error = "Error al obtener cursos: ${e.message}"
                    )
                }
            }
        }
    }

    fun selectCourse(course: Course) {
        _uiState.update { it.copy(selectedCourse = course) }
    }
}
