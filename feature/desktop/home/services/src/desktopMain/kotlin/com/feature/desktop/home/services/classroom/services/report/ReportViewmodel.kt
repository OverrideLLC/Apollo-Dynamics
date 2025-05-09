package com.feature.desktop.home.services.classroom.services.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.desktop.home.services.utils.PdfGenerator
import com.google.api.services.classroom.model.Course
import com.network.repositories.contract.ClassroomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class para representar el estado de la UI del reporte
// Data class to represent the state of the report UI
data class ReportUiState(
    val isLoading: Boolean = false,
    val report: CourseReport? = null, // Datos del reporte estructurado
    val error: String? = null,
    val isFilePickerVisible: Boolean = false, // Controla la visibilidad del selector de archivos
    val pdfGenerated: Boolean = false, // Indica si el PDF se generó exitosamente
    val pdfFilePath: String? = null,
    val courses: List<Course>? = null,
    val selectedCourse: Course? = null
)

// Data class to represent a student's submission for a specific coursework
// Clase de datos para representar la entrega de un estudiante para un trabajo de clase específico
data class StudentSubmissionDetail(
    val courseworkTitle: String,
    val submissionState: String, // e.g., "CREATED", "TURNED_IN", "RETURNED", "RECLAIMED_BY_STUDENT"
    val assignedGrade: Double? = null,
    val maxPoints: Double? = null
    // Add other relevant submission details you need for the report
    // Añadir otros detalles relevantes de la entrega que necesites para el reporte
)

// Data class to represent a student with their submissions
// Clase de datos para representar un estudiante con sus entregas
data class StudentReport(
    val studentId: String?,
    val studentName: String?,
    val studentEmail: String?,
    val submissions: List<StudentSubmissionDetail>
)

// Data class to represent the overall report structure
// Clase de datos para representar la estructura general del reporte
data class CourseReport(
    val courseId: String,
    val courseName: String?, // You might want to fetch course details as well
    val studentReports: List<StudentReport>
)

class ReportViewmodel(
    private val repositoryClassroom: ClassroomRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val courses = repositoryClassroom.getAllCourses()
            _uiState.update {
                it.copy(courses = courses, isLoading = false)
            }
        }
    }

    /**
     * Inicia el proceso de generación del reporte.
     * Si `showFilePicker` es true, muestra el selector de archivos.
     * Si `showFilePicker` es false y `filePath` se proporciona, genera el PDF.
     *
     * Starts the report generation process.
     * If `showFilePicker` is true, shows the file picker.
     * If `showFilePicker` is false and `filePath` is provided, generates the PDF.
     *
     * @param classId El ID de la clase.
     * @param showFilePicker Indica si se debe mostrar el selector de archivos.
     * @param filePath La ruta del archivo seleccionada (usar cuando `showFilePicker` es false).
     */
    fun startReportGeneration(
        course: Course,
        showFilePicker: Boolean = false,
        filePath: String? = null
    ) {
        _uiState.update {
            it.copy(selectedCourse = course)
        }

        if (showFilePicker) {
            _uiState.update { it.copy(isFilePickerVisible = true) }
        } else if (filePath != null && uiState.value.selectedCourse != null) {
            // Si ya tenemos la ruta y el ID de la clase, procedemos a generar el reporte y PDF
            // If we already have the file path and class ID, proceed to generate the report and PDF
            generateReportAndPdf(uiState.value.selectedCourse!!, filePath)
        }
    }

    /**
     * Oculta el selector de archivos.
     * Hides the file picker.
     */
    fun hideFilePicker() {
        _uiState.update { it.copy(isFilePickerVisible = false, selectedCourse = null) }
    }

    /**
     * Genera el reporte y el archivo PDF.
     * Esta función es privada y solo se llama después de seleccionar la ruta del archivo.
     *
     * Generates the report and the PDF file.
     * This function is private and is only called after the file path is selected.
     *
     * @param classId El ID de la clase.
     * @param filePath La ruta donde se guardará el archivo PDF.
     */
    private fun generateReportAndPdf(
        course: Course,
        filePath: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    report = null,
                    pdfGenerated = false,
                    pdfFilePath = null
                )
            }
            runCatching {
                // 1. Fetch students for the course
                // 1. Obtener estudiantes para el curso
                val students = repositoryClassroom.getCourseStudents(course.id)

                // 2. Fetch coursework for the course
                // 2. Obtener trabajos de clase para el curso
                val courseworkList = repositoryClassroom.getCourseWork(course.id)

                // 3. Create a map to store student reports
                // 3. Crear un mapa para almacenar los reportes de los estudiantes
                val studentReportsMap = mutableMapOf<String, MutableList<StudentSubmissionDetail>>()

                // Initialize the map with each student
                // Inicializar el mapa con cada estudiante
                students.forEach { student ->
                    student.userId?.let { studentId ->
                        studentReportsMap[studentId] = mutableListOf()
                    }
                }

                // 4. Fetch submissions for each coursework item and populate the map
                // 4. Obtener entregas para cada trabajo de clase y popular el mapa
                for (coursework in courseworkList) {
                    coursework.id?.let { courseworkId ->
                        val submissions =
                            repositoryClassroom.getStudentSubmissions(course.id, courseworkId)
                        for (submission in submissions) {
                            submission.userId?.let { studentId ->
                                val submissionDetail = StudentSubmissionDetail(
                                    courseworkTitle = coursework.title ?: "Untitled Coursework",
                                    submissionState = submission.state ?: "UNKNOWN",
                                    assignedGrade = submission.assignedGrade
                                        ?: 0.0, // Asegúrate de manejar Double?
                                    maxPoints = coursework.maxPoints
                                        ?: 0.0 // Asegúrate de manejar Double?
                                )
                                studentReportsMap[studentId]?.add(submissionDetail)
                            }
                        }
                    }
                }

                // 5. Construct the final list of StudentReport objects
                // 5. Construir la lista final de objetos StudentReport
                val studentReports = students.mapNotNull { student ->
                    student.userId?.let { studentId ->
                        StudentReport(
                            studentId = studentId,
                            studentName = student.profile?.name?.fullName, // Assuming you have profile data
                            studentEmail = student.profile?.emailAddress, // Assuming you have profile data
                            submissions = studentReportsMap[studentId] ?: emptyList()
                        )
                    }
                }

                CourseReport(
                    courseId = course.id,
                    courseName = course.name,
                    studentReports = studentReports
                )

            }.onSuccess { courseReport ->
                // Generar PDF después de obtener y procesar los datos del reporte
                // Generate PDF after fetching and processing report data
                val pdfSuccess = PdfGenerator().generateReportPdf(courseReport, filePath)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        report = courseReport,
                        pdfGenerated = pdfSuccess,
                        pdfFilePath = if (pdfSuccess) filePath else null
                    )
                }

            }.onFailure { e ->
                println("Error generating report: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error generating report: ${e.message}",
                        pdfGenerated = false,
                        pdfFilePath = null
                    )
                }
            }
        }
    }

    fun selectCourse(course: Course) {
        _uiState.update {
            it.copy(selectedCourse = course)
        }
    }
}
