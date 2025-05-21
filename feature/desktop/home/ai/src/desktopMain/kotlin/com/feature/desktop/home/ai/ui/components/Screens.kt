package com.feature.desktop.home.ai.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel.AiState
import com.feature.desktop.home.services.classroom.services.announcement.ClassroomAnnouncementScreen
import com.feature.desktop.home.services.classroom.services.announcement.ClassroomAnnouncements
import com.feature.desktop.home.services.classroom.services.report.ReportScreen
import com.feature.desktop.home.services.classroom.services.uploadassignment.UploadAssignmentScreen
import com.shared.resources.Res
import com.shared.resources.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.campaign_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Frame

@Composable
internal fun Screens(
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
                    onUploadSuccess = { viewModel.update { copy(work = null) } },
                    onError = {}
                )
            },
            icon = Res.drawable.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
            name = "Work",
            close = { viewModel.update { copy(work = null) } },
        )
    }
}
