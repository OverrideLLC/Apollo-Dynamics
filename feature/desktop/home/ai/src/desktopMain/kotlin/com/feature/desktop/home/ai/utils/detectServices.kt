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
        "Classroom ejecutar" -> {
            viewModel.runCode(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom anunciar" -> {
            viewModel.announceClassroom(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom anuncios" -> {
            viewModel.getAnnouncementsClassroom()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom reporte" -> {
            viewModel.showReportClassroom()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom subir tarea" -> {
            viewModel.updateAssignmentClassroom(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "Classroom analizar trabajo" -> {
            viewModel.workerRanking()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "anunciar" -> {
            viewModel.announceLocal(message)
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "anuncios" -> {
            viewModel.getAnnouncements()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "reporte" -> {
            viewModel.showReport()
            valueChange(TextFieldValue(""))
            onNewMessage("")
        }

        "subir tarea" -> {
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