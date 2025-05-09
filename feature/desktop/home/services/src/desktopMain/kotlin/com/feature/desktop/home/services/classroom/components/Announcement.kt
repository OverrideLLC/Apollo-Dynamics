package com.feature.desktop.home.services.classroom.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementState
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementViewModel
import com.feature.desktop.home.services.classroom.utils.enums.ClassroomServices

@Composable
internal fun Announcement(
    state: ClassroomAnnouncementState,
    announcement: String,
    viewModel: ClassroomAnnouncementViewModel
) {
    val service = ClassroomServices.ADD_ANNOUNCE
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.Start
            ),
        ) {
            CardService(
                icon = service.icon,
                title = service.title,
                size = 140.dp,
                onClick = {
                    state.selectedCourseId?.let { courseId ->
                        when (service) {
                            ClassroomServices.ADD_ANNOUNCE -> {
                                viewModel.addAnnouncement(
                                    content = announcement,
                                    courseId = courseId
                                )
                            }
                        }
                    }
                },
            )
        }
    }
}
