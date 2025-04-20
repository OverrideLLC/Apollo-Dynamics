package com.feature.desktop.home.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.network.interfaces.GeminiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing

class AiViewModel(
    private val repository: GeminiRepository
) : ViewModel() {
    data class Message(
        val text: String,
        val isUser: Boolean
    )

    data class AiState(
        var isLoading: Boolean = false,
        var messages: List<Message> = emptyList(),
        var newMessage: String = ""
    )

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    fun update(update: AiState.() -> AiState) {
        _state.value = update(_state.value)
    }

    fun sendMessage(message: String) {
        update {
            copy(
                messages = messages + Message(message, true),
                newMessage = ""
            )
        }
        viewModelScope.launch(Dispatchers.Swing){
            try {
                update { copy(isLoading = true) }
                val response = repository.generate(message)
                update {
                    copy(
                        messages = messages + Message(response, false),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                update { copy(isLoading = false) }
                e.printStackTrace()
            }
        }
    }
}