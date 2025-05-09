package com.feature.desktop.home.services.classroom.services.announcement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementState
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementViewModel
import com.feature.desktop.home.services.classroom.components.ListCourses
import com.feature.desktop.home.services.classroom.components.AnnouncementCreator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClassroomAnnouncementScreen(
    announcement: String
) = Screen(announcement)

@Composable
internal fun Screen(
    announcement: String,
    viewModel: ClassroomAnnouncementViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    Content(state, viewModel, announcement)
}

@Composable
internal fun Content(
    state: ClassroomAnnouncementState,
    viewModel: ClassroomAnnouncementViewModel,
    announcement: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.surface),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Class Announcements",
                style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.onSurface
            )

            ListCourses(
                state = state,
                onCourseClick = { course -> viewModel.onCourseSelected(course) }
            )

            if (state.selectedCourseId != null) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = colorScheme.onSurface
                )
            }


            state.selectedCourseId?.let {
                AnnouncementCreator(
                    state = state,
                    viewModel = viewModel,
                    announcement = announcement,
                )
            }
        }
    }
}