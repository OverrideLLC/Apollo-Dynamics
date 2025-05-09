package com.feature.desktop.home.services.classroom.services.announcements

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
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementViewModel
import com.feature.desktop.home.services.classroom.components.AnnouncementAll
import com.feature.desktop.home.services.classroom.components.ListCourses
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClassroomAnnouncements() {
    val viewModel: ClassroomAnnouncementViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
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
                selectedCourseId = state.selectedCourseId,
                courses = state.courses,
                onCourseClick = { course ->
                    viewModel.onCourseSelected(course)
                    viewModel.getCourseAnnouncements(course.id)
                }
            )

            if (state.selectedCourseId != null) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = colorScheme.onSurface
                )
            }

            AnnouncementAll(
                state = state,
                onAnnouncementClick = { announcement -> viewModel.onAnnouncementSelected(announcement) },
                viewModel = viewModel
            )
        }
    }
}