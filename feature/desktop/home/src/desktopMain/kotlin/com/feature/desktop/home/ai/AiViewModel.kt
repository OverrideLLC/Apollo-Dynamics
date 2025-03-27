package com.feature.desktop.home.ai

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AiViewModel : ViewModel() {
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
    }
}