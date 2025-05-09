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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.feature.desktop.home.ai.ui.components.Chat
import com.feature.desktop.home.ai.ui.components.SelectedFilesPreview
import com.feature.desktop.home.ai.ui.components.TextFieldAi
import com.feature.desktop.home.ai.ui.components.fileChooserDialog
import com.feature.desktop.home.ai.ui.screen.AiViewModel.AiState
import com.feature.desktop.home.services.classroom.services.announcement.ClassroomAnnouncementScreen
import com.feature.desktop.home.services.classroom.services.announcements.ClassroomAnnouncements
import com.feature.desktop.home.services.classroom.services.report.ReportScreen
import com.feature.desktop.home.services.classroom.uploadassignment.UploadAssignmentScreen
import com.shared.resources.Res
import com.shared.resources.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Frame

@Composable
fun AiScreen() = Screen()

@Composable
internal fun Screen(viewModel: AiViewModel = koinViewModel()) {
    LaunchedEffect(Unit) { viewModel.loadData() }
    val state by viewModel.state.collectAsState()
    val messages = state.messages
    val newMessage = remember { mutableStateOf("") }
    var showFileChooser by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(TextFieldValue(newMessage.value)) }

    Screens(
        viewModel = viewModel,
        state = state,
        showFileChooserState = showFileChooser,
        showFileChooser = { showFileChooser = it }
    )

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
                value = value,
                onValueChange = { value = it },
                state = state,
                modifier = Modifier.padding(
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                onAttachFile = { showFileChooser = true },
                onSend = { detectedService, message ->
                    when (detectedService) {
                        "run" -> {
                            viewModel.runCode(message)
                            value = TextFieldValue("")
                            newMessage.value = ""
                        }

                        "classroom announce" -> {
                            viewModel.announce(message)
                            value = TextFieldValue("")
                            newMessage.value = ""
                        }

                        "classroom announcements" -> {
                            viewModel.getAnnouncements()
                            value = TextFieldValue("")
                            newMessage.value = ""
                        }

                        "classroom report" -> {
                            viewModel.showReport()
                            value = TextFieldValue("")
                            newMessage.value = ""
                        }

                        "classroom update assignment" -> {
                            viewModel.updateAssignment(message)
                            value = TextFieldValue("")
                            newMessage.value = ""
                        }

                        else -> {
                            if (value.text.isNotBlank() && !state.isLoading) {
                                viewModel.sendMessage(value.text)
                                value = TextFieldValue("")
                                newMessage.value = ""
                            }
                        }
                    }
                }
            )
        } else {
            CircularProgressIndicator()
        }
    }
}


@Composable
private fun Screens(
    viewModel: AiViewModel = koinViewModel(),
    state: AiState,
    showFileChooserState: Boolean,
    showFileChooser: (Boolean) -> Unit,
) {
    if (showFileChooserState) {
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
                showFileChooser(false)
                if (selectedFiles.isNotEmpty()) {
                    viewModel.addFiles(selectedFiles)
                }
            }
        }
    }

    state.announcement?.let {
        if (!state.isLoading) {
            ScreenAction(
                size = DpSize(900.dp, 600.dp),
                content = { ClassroomAnnouncementScreen(announcement = it) },
                close = { viewModel.update { copy(announcement = null) } },
                icon = Res.drawable.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                name = "Announce",
            )
        }
    } ?: run { }

    if (state.announcements) {
        ScreenAction(
            size = DpSize(900.dp, 600.dp),
            content = { ClassroomAnnouncements() },
            close = { viewModel.update { copy(announcements = false) } },
            icon = Res.drawable.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
            name = "Announcements",
        )
    }

    if (state.showReport) {
        ScreenAction(
            size = DpSize(900.dp, 600.dp),
            content = { ReportScreen() },
            close = { viewModel.update { copy(showReport = false) } },
            icon = Res.drawable.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
            name = "Report",
        )
    }

    state.work?.let { work ->
        ScreenAction(
            size = DpSize(900.dp, 600.dp),
            content = {
                UploadAssignmentScreen(
                    initialAssignmentTitle = work.title,
                    initialAssignmentDescription = work.description,
                    onUploadSuccess = {},
                    onError = {}
                )
            },
            icon = Res.drawable.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
            name = "Work",
            close = { viewModel.update { copy(work = null) } },
        )
    }
}