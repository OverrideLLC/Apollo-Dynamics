package com.feature.desktop.home.ai.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.feature.desktop.home.ai.ui.components.Chat
import com.feature.desktop.home.ai.ui.components.SelectedFilesPreview
import com.feature.desktop.home.ai.ui.components.TextFieldAi
import com.feature.desktop.home.ai.ui.components.fileChooserDialog
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Frame

@Composable
fun AiScreen() = Screen()

@Composable
internal fun Screen(
    viewModel: AiViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadData() }
    val state by viewModel.state.collectAsState()
    val messages = state.messages
    val newMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var showFileChooser by remember { mutableStateOf(false) }

    if (showFileChooser) {
        AwtWindow(
            create = {
                object : Frame() {
                    init {
                        isVisible = false
                    }
                }
            },
            dispose = { frame -> frame.dispose() }
        ) { frame ->
            fileChooserDialog(parent = frame) { selectedFiles ->
                showFileChooser = false
                if (selectedFiles.isNotEmpty()) {
                    viewModel.addFiles(selectedFiles)
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (!state.isLoadingData) {
            Chat(
                messages = messages,
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                viewModel = viewModel
            )
            SelectedFilesPreview(
                files = state.selectedFiles,
                onRemoveFile = viewModel::removeFile // Pasa la función para eliminar
                // El modifier se configura dentro del composable para padding, etc.
            )
            TextFieldAi(
                value = newMessage.value,
                onValueChange = { newMessage.value = it },
                state = state,
                modifier = Modifier.padding(
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                onAttachFile = { showFileChooser = true },
                onSend = {
                    if (newMessage.value.isNotBlank() && !state.isLoading) {
                        scope.launch {
                            viewModel.sendMessage(newMessage.value)
                            newMessage.value = ""
                        }
                    }
                }
            )
        } else {
            CircularProgressIndicator()
        }
    }
}
