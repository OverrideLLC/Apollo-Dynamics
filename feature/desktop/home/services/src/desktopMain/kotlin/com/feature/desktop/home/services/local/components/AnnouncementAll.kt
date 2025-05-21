package com.feature.desktop.home.services.local.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feature.desktop.home.services.local.services.announcement.LocalAnnouncementState
import com.feature.desktop.home.services.local.services.announcement.LocalAnnouncementViewModel
import com.google.api.services.classroom.model.Announcement
import com.shared.resources.Res
import com.shared.resources.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AnnouncementAllLocal(
    viewModel: LocalAnnouncementViewModel,
    state: LocalAnnouncementState,
    onAnnouncementClick: (Announcement) -> Unit
) {
    val scrollStateCourses = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Available Courses",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
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
                items(items = state.announcements) { announcement ->
                    AnnouncementWidget(
                        announcement = announcement,
                        size = 200.dp,
                        onCLickDeleted = {
                            viewModel.removeAnnouncement(
                                announcementId = announcement.id,
                                courseId = state.selectedCourseId ?: ""
                            )
                        },
                        onClick = {
                            onAnnouncementClick(announcement)
                        }
                    )
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

@Composable
internal fun AnnouncementWidget(
    announcement: Announcement,
    size: Dp,
    onCLickDeleted: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = onCLickDeleted,
                modifier = Modifier.size(30.dp).align(Alignment.End),
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.delete_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                        modifier = Modifier.fillMaxSize(),
                        tint = colorScheme.error,
                        contentDescription = ""
                    )
                }
            )
            Text(
                text = announcement.text,
                style = typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                color = colorScheme.onSurface
            )
        }
    }
}