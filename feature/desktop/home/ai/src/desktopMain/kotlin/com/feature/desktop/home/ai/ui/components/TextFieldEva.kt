package com.feature.desktop.home.ai.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel.AiState
import com.feature.desktop.home.ai.utils.detectServices
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TextFieldEva(
    viewModel: AiViewModel = koinViewModel(),
    state: AiState,
    value: TextFieldValue,
    showFileChooser: (Boolean) -> Unit,
    onNewMessage: (String) -> Unit,
    onChangeValue: (TextFieldValue) -> Unit
) {
    SelectedFilesPreview(
        files = state.selectedFiles,
        onRemoveFile = viewModel::removeFile
    )
    TextFieldAi(
        value = value,
        onValueChange = { onChangeValue(it) },
        state = state,
        modifier = Modifier.padding(
            bottom = 8.dp,
            start = 16.dp,
            end = 16.dp
        ),
        onAttachFile = { showFileChooser(true) },
        onSend = { detectedService, message ->
            detectServices(
                message = message,
                viewModel = viewModel,
                state = state,
                value = value,
                detectedService = detectedService ?: "",
                onNewMessage = { onNewMessage(it) },
                valueChange = { onChangeValue(it) }
            )
        }
    )
}
