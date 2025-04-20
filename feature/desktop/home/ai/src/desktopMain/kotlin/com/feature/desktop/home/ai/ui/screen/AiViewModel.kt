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
        val selectedFiles: List<File> = emptyList()
    )

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

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
        // Solo proceder si hay texto o archivos, y no está cargando
        if ((messageText.isBlank() && state.value.selectedFiles.isEmpty()) || state.value.isLoading || state.value.chat == null) {
            return
        }

        val filesToSend = state.value.selectedFiles // Copia la lista actual de archivos

        // Añadir mensaje del usuario a la UI inmediatamente
        _state.update { currentState ->
            currentState.copy(
                messages = currentState.messages + Message(text = messageText, isUser = true), // Podrías añadir los 'filesToSend' aquí si Message los soporta
                errorMessage = null,
                selectedFiles = emptyList() // Limpiar archivos seleccionados de la UI después de enviarlos
            )
        }

        viewModelScope.launch(Dispatchers.IO) { // Usar IO para la llamada de red y posible lectura de archivos
            try {
                _state.update { it.copy(isLoading = true) } // Indicar carga

                // Llamar al repositorio con el texto y los archivos
                val response = repository.sendMessage(
                    chat = state.value.chat!!, // Sabemos que no es nulo por la comprobación inicial
                    message = messageText,
                    files = filesToSend // Pasar la lista de archivos
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
                        messages = currentState.messages + Message(text = "Error: ${e.localizedMessage}", isUser = false)
                    )
                }
            }
        }
    }

    fun runCode(
        text: String
    ) {
        _state.update { it.copy(isRunCode = true) }
        viewModelScope.launch(Dispatchers.Swing) {
            try {
                val response = repository.generate(
                    prompt = """
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
                        messages = currentState.messages + Message(text = "Error: ${e.localizedMessage}", isUser = false)
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
}
