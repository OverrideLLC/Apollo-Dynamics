package com.feature.desktop.home.ai.ui.screen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.network.interfaces.GeminiRepository
import dev.shreyaspatil.ai.client.generativeai.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AiViewModel(
    private val repository: GeminiRepository
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
        val showReport: Boolean = false
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

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
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

    fun classroom(message: String) {
    }

    fun announce(message: String) {
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

    fun getAnnouncements() {
        _state.update { it.copy(announcements = true) }
    }

    fun showReport() {
        _state.update { it.copy(showReport = true) }
    }
}
