package com.feature.desktop.home.ai.utils

import androidx.compose.ui.text.input.TextFieldValue
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel.AiState

internal fun detectServices(
    message: String,
    viewModel: AiViewModel,
    state: AiState,
    value: TextFieldValue,
    detectedService: String,
    onNewMessage: (String) -> Unit,
    valueChange: (TextFieldValue) -> Unit
) {
    when (detectedService) {
        "run" -> {
            viewModel.runCode(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "classroom announce" -> {
            viewModel.announce(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "classroom announcements" -> {
            viewModel.getAnnouncements()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "classroom report" -> {
            viewModel.showReport()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "classroom update assignment" -> {
            viewModel.updateAssignment(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        else -> {
            if (value.text.isNotBlank() && !state.isLoading) {
                viewModel.sendMessage(value.text)
                valueChange(TextFieldValue(""))
                onNewMessage("")
            }
        }
    }
}