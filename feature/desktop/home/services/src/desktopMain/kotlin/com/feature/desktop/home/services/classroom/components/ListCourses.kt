package com.feature.desktop.home.services.classroom.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementState
import com.google.api.services.classroom.model.Course
import com.shared.resources.Res
import com.shared.resources.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ClassWidget

@Composable
fun ListCourses(
    state: ClassroomAnnouncementState,
    onCourseClick: (Course) -> Unit
) {
    val scrollStateCourses = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Available Courses",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.Start
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
                state = scrollStateCourses
            ) {
                state.courses?.let { courses ->
                    items(items = courses) { course ->
                        ClassWidget(
                            name = course.name ?: "No name",
                            color = colorScheme.primary,
                            career = course.descriptionHeading ?: "",
                            degree = "",
                            section = course.section ?: "General",
                            onClick = { onCourseClick(course) },
                            delete = {},
                            isSelected = course.id == state.selectedCourseId,
                            isEnabledDeleted = false,
                            iconDeleted = Res.drawable.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
                        )
                    }
                } ?: item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp).padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp),
                adapter = rememberScrollbarAdapter(
                    scrollState = scrollStateCourses
                )
            )
        }
    }
}
