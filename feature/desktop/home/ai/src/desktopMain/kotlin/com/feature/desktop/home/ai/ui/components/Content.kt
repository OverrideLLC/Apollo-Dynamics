package com.feature.desktop.home.ai.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel.AiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun Content(
    viewModel: AiViewModel = koinViewModel(),
    state: AiState,
    displayedText: String,
    alpha: Animatable<Float, *>,
    showFileChooser: (Boolean) -> Unit
) {
    val messages = state.messages
    val newMessage = remember { mutableStateOf("") }
    var value by remember { mutableStateOf(TextFieldValue(newMessage.value)) }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.messages.isNotEmpty()) {
            Chat(
                messages = messages,
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                viewModel = viewModel
            )
        } else {
            Eva(
                displayedText = displayedText,
                alpha = alpha
            )
        }
        TextFieldEva(
            viewModel = viewModel,
            state = state,
            value = value,
            showFileChooser = showFileChooser,
            onNewMessage = { newMessage.value = it },
            onChangeValue = { value = it }
        )
    }
}
