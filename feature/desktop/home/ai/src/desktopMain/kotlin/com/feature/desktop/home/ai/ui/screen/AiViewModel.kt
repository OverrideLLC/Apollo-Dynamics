package com.feature.desktop.home.ai.ui.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.desktop.home.ai.utils.WorkData
import com.google.api.services.classroom.model.Student // Necesario para el nombre del estudiante
import com.network.interfaces.GeminiRepository
import com.network.repositories.contract.ClassroomRepository
import dev.shreyaspatil.ai.client.generativeai.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing // Asegúrate que es el dispatcher correcto para tu UI
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.google.api.services.classroom.model.UserProfile // Para obtener el nombre del estudiante

class AiViewModel(
    private val repository: GeminiRepository,
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    @Immutable
    data class Message @OptIn(ExperimentalUuidApi::class) constructor(
        val id: String = Uuid.random().toString(),
        val text: String,
        val isUser: Boolean,
    )

    @Immutable
    data class AiState(
        val isLoading: Boolean = false,
        val isLoadingData: Boolean = true,
        val isRunCode: Boolean = false,
        val messages: List<Message> = emptyList(),
        val errorMessage: String? = null,
        val chat: Chat? = null,
        val selectedService: String? = null,
        val selectedFiles: List<File> = emptyList(),
        val announcement: String? = null,
        val announcements: Boolean = false,
        val showReport: Boolean = false,
        val work: WorkData? = null,
        val workText: String? = null
    )

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    fun update(update: AiState.() -> AiState) {
        _state.value = _state.value.update()
    }

    fun loadData() {
        println("AiViewModel: Cargando datos...")
        _state.update { it.copy(isLoadingData = true, errorMessage = null) }
        viewModelScope.launch(Dispatchers.Swing) {
            try {
                val chat = repository.startChat()
                _state.update {
                    it.copy(chat = chat, isLoadingData = false)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingData = false,
                        errorMessage = "Error al iniciar el chat: ${e.localizedMessage ?: "Error desconocido"}"
                    )
                }
                e.printStackTrace()
            }
        }
    }

    fun addFiles(files: List<File>) {
        _state.update { currentState ->
            // Evitar duplicados si se seleccionan los mismos archivos de nuevo
            val currentFiles = currentState.selectedFiles.toSet()
            val newFiles = files.filter { it !in currentFiles }
            currentState.copy(selectedFiles = currentState.selectedFiles + newFiles)
        }
    }

    // Función para quitar un archivo seleccionado
    fun removeFile(file: File) {
        _state.update { currentState ->
            currentState.copy(selectedFiles = currentState.selectedFiles - file)
        }
    }

    fun sendMessage(messageText: String) {
        if ((messageText.isBlank() && state.value.selectedFiles.isEmpty()) || state.value.isLoading || state.value.chat == null) return

        val filesToSend = state.value.selectedFiles

        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages + Message(text = messageText, isUser = true),
                errorMessage = null,
                selectedFiles = emptyList()
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true) }

                val response = repository.sendMessage(
                    chat = state.value.chat!!,
                    message = messageText,
                    files = filesToSend
                )

                // Añadir respuesta del modelo a la UI
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(text = response, isUser = false),
                        isLoading = false // Finalizar carga
                    )
                }
            } catch (e: Exception) {
                // Manejar error
                _state.update { currentState ->
                    // Restaurar los archivos seleccionados si hubo un error para que el usuario pueda reintentar
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Error al obtener respuesta: ${e.localizedMessage ?: "Error desconocido"}",
                        selectedFiles = filesToSend // Restaurar archivos en caso de error
                    )
                }
                e.printStackTrace()
                // Opcionalmente, añadir un mensaje de error a la lista de mensajes
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(
                            text = "Error: ${e.localizedMessage}",
                            isUser = false
                        )
                    )
                }
            }
        }
    }

    fun runCode(
        text: String
    ) {
        _state.update { it.copy(isRunCode = true) }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = repository.generate(
                    prompt = """
                        ==============EN LA RESPUESTA SOLO QUIERO EL RESULTADO DE EL CODIGO QUE EJECUTASTE==========
                        Ejecuta este codigo:
                        ```python
                        $text
                        ```
                    """.trimIndent()
                )
                _state.update { it.copy(isRunCode = false) }
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(text = response, isUser = false)
                    )
                }
                println("AiViewModel: $response")
            } catch (e: Exception) {
                _state.update { it.copy(isRunCode = false) }
                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + Message(
                            text = "Error: ${e.localizedMessage}",
                            isUser = false
                        )
                    )
                }
            }
        }
    }

    fun clearChat() {
        _state.update {
            it.copy(
                isLoadingData = true,
                messages = emptyList(),
                chat = null
            )
        }
        loadData()
    }

    fun announceClassroom(message: String) {
        _state.update { currentState ->
            currentState.copy(
                announcement = message,
                isLoading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val text = repository.generate(
                    """
                        Redacta:
                        [${_state.value.announcement}]".
                        Instrucciones:
                        •"Mejora la redacción sin modificar el sentido del texto."
                        •"Asegúrate que sea un anuncio para estudiantes." 
                        *"El texto resultante debe ser adecuado para publicarse en Google Classroom"
                        No agregues ni elimines información. El mensaje debe ser el mismo, solo debe de estar escrito correctamente.
                        Solo entrega el texto modificado, no ponga ninguna cosa extra, ningun mensaje tuyo, es el mensaje final.
                    """.trimIndent()
                )
                _state.update { it.copy(announcement = text, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAnnouncementsClassroom() {
        _state.update { it.copy(announcements = true) }
    }

    fun showReportClassroom() {
        _state.update { it.copy(showReport = true) }
    }

    fun updateAssignmentClassroom(message: String) {
        _state.update { it.copy(workText = message, isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val description = repository.generateAdvancedPrompt(
                    """
                        =============EN LA RESPUESTA SOLO QUIERO LA DESCRIPCION DE LA TAREA NO AGREGUES NINGUNA INSTRUCCION NI EXPLICACION==========
                        [${_state.value.workText}]".
                        nstrucciones:
                        1.Contexto: Describe brevemente el tema o unidad en la que se enmarca la tarea
                        2.Objetivo: Define qué se espera que los alumnos aprendan o logren al completar la tarea.
                        3.Actividad: Explica con claridad qué deben hacer los alumnos paso a paso. Incluye detalles específicos sobre los recursos, herramientas o materiales que necesitan.
                        4.Formato de entrega: Especifica el formato en el que se debe de entregar la asignación.
                        5.Agrega enlaces a recurso: 
                            Agrega por lo menos 4 videos de youtube para que los alumnos tengan recursos de el tema.
                            Solo da los links y nombres de los videos, pero no lo agregues en formato markdown, asi no ()[] x, asi si https://example.com.
                        Consideraciones:
                            •No lo pongas en formato markdown, porque se va a subir a classroom y ahi no existe le formato markdrown, damelo en un formato limpio.
                            •Usa un lenguaje motivador y positivo.
                            •Sé preciso y evita la ambigüedad.
                            •Incluye instrucciones claras y numeradas si es necesario.
                            •Adapta el tono y el lenguaje al nivel educativo (por ejemplo, primaria, secundaria, universidad).
                            •Ofrece ejemplos o aclaraciones si es pertinente.
                            •Menciona cualquier información de apoyo que el estudiante pueda utilizar.                            
                        =============EN LA RESPUESTA SOLO QUIERO LA DESCRIPCION DE LA TAREA NO AGREGUES NINGUNA INSTRUCCION NI EXPLICACION==========
                    """.trimIndent()
                )

                val title = repository.generate(
                    """
                        =============EN LA RESPUESTA SOLO QUIERO EL TITULO DE LA TAREA==========
                        Genera un titulo para la tarea: ${_state.value.workText}
                        =============EN LA RESPUESTA SOLO QUIERO EL TITULO DE LA TAREA RECUERDA QUE ESTO LO VA A VER EL USUARIO FINAL==========
                    """.trimIndent()
                )

                val work = WorkData(
                    description = description,
                    title = title
                )
                _state.update {
                    it.copy(workText = null, work = work)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun workerRanking() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val courses = classroomRepository.getAllCourses()
                if (courses.isEmpty()) {
                    _state.update { it.copy(isLoading = false) }
                    return@launch
                }

                var reviewsCount = 0
                val allStudentProfiles = mutableMapOf<String, UserProfile?>()

                for (course in courses) {
                    _state.update { it.copy() }

                    // Obtener todos los perfiles de estudiantes del curso una vez para optimizar
                    // classroomRepository.getCourseStudents(course.id) devuelve List<Student>,
                    // Student tiene userId y profile (UserProfile).
                    // Necesitamos una forma de obtener UserProfile por userId de forma eficiente.
                    // Por ahora, asumiremos que StudentSubmission podría tenerlo o lo obtendremos por separado si es necesario.
                    // Esta parte puede necesitar ajuste según cómo tu `ClassroomRepository` pueda obtener UserProfile.
                    // Para simplificar, intentaremos obtenerlo de la sumisión, o usaremos el ID.

                    // Obtener perfiles de estudiantes para el curso actual
                    val studentsInCourse = classroomRepository.getCourseStudents(course.id)
                    studentsInCourse.forEach { student ->
                        if (student.userId != null && student.profile != null) {
                            allStudentProfiles[student.userId] = student.profile
                        }
                    }


                    val courseWorks = classroomRepository.getCourseWork(course.id)
                    if (courseWorks.isEmpty()) {
                        _state.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages + Message(text = "Curso: ${course.name} - No hay tareas para revisar.", isUser = false)
                            )
                        }
                        continue
                    }

                    for (courseWork in courseWorks) {
                        _state.update { it.copy() }
                        val submissions = classroomRepository.getStudentSubmissions(course.id, courseWork.id)

                        if (submissions.isEmpty()) {
                            _state.update { currentState ->
                                currentState.copy(
                                    messages = currentState.messages + Message(text = "Tarea: ${courseWork.title} (Curso: ${course.name}) - No hay entregas.", isUser = false)
                                )
                            }
                            continue
                        }

                        for (submission in submissions) {
                            // Intentar obtener el nombre del estudiante
                            // StudentSubmission tiene userId. Necesitamos obtener el UserProfile.
                            // La API de Classroom podría no devolver UserProfile directamente en StudentSubmission en todos los casos.
                            // Si `submission.profile` no existe o es nulo, usamos el `userId`.
                            // Una mejor aproximación sería tener un método en ClassroomRepository para obtener UserProfile por ID.
                            val studentProfile = allStudentProfiles[submission.userId]
                            val studentName = studentProfile?.name?.fullName ?: submission.userId ?: "Alumno Desconocido"

                            // Contenido del trabajo: Usamos alternateLink como placeholder.
                            // En un caso real, necesitarías descargar y procesar el contenido de este enlace.
                            val workContentProxy = submission.alternateLink ?: "No hay enlace al trabajo."
                            // También se podría usar submission.assignmentSubmission.attachments si se quiere procesar archivos.
                            // Por ahora, nos enfocamos en el enlace como un resumen o descripción.

                            val prompt = """
                                Por favor, evalúa el siguiente trabajo de un alumno.
                                Alumno: $studentName
                                Tarea: ${courseWork.title}
                                Enlace/Contenido del trabajo (o descripción): $workContentProxy

                                Proporciona:
                                1. Una descripción muy breve de tu evaluación (máximo 2 frases).
                                2. Una calificación numérica del 1 al 100.
                                3. Indica si el trabajo es: Excelente, Bueno, Regular, Necesita Mejorar.

                                Formato de respuesta esperado (solo esto, sin texto adicional):
                                Evaluación: [Tu descripción aquí]
                                Calificación: [Número entre 1 y 100]
                                Desempeño: [Excelente/Bueno/Regular/Necesita Mejorar]
                            """.trimIndent()

                            try {
                                _state.update { it.copy() }
                                val aiResponse = repository.generate(prompt)

                                // Parsear la respuesta de la IA (esto es una simplificación)
                                val evalRegex = Regex("Evaluación: (.*?)\nCalificación: (.*?)\nDesempeño: (.*)", RegexOption.DOT_MATCHES_ALL)
                                val matchResult = evalRegex.find(aiResponse)

                                val reviewMessage: String
                                if (matchResult != null) {
                                    val (description, grade, performance) = matchResult.destructured
                                    reviewMessage = """
                                        Revisión para: $studentName
                                        Tarea: ${courseWork.title} (Curso: ${course.name})
                                        Descripción IA: $description
                                        Calificación IA: $grade
                                        Desempeño IA: $performance
                                    """.trimIndent()
                                } else {
                                    reviewMessage = "Respuesta de IA para $studentName (Tarea: ${courseWork.title}):\n$aiResponse\n(No se pudo parsear en el formato esperado)"
                                }

                                _state.update { currentState ->
                                    currentState.copy(
                                        messages = currentState.messages + Message(text = reviewMessage, isUser = false)
                                    )
                                }
                                reviewsCount++
                            } catch (e: Exception) {
                                val errorMessage = "Error evaluando trabajo de $studentName para '${courseWork.title}': ${e.message}"
                                println(errorMessage)
                                _state.update { currentState ->
                                    currentState.copy(
                                        messages = currentState.messages + Message(text = errorMessage, isUser = false),
                                        errorMessage = (currentState.errorMessage ?: "") + "\n" + errorMessage
                                    )
                                }
                            }
                        }
                    }
                }
                _state.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error en workerRanking: ${e.localizedMessage ?: "Error desconocido"}"
                    )
                }
                e.printStackTrace()
            }
        }
    }



    fun announceLocal(string: String) {}
    fun getAnnouncements() {}
    fun showReport() {}
    fun updateAssignment(string: String) {}
}
