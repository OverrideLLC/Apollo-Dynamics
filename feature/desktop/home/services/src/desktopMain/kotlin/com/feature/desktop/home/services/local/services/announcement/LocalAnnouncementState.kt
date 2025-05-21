package com.feature.desktop.home.services.local.services.announcement

import androidx.compose.runtime.Immutable
import com.google.api.services.classroom.model.Announcement
import com.override.data.entity.ClassEntity

@Immutable
data class LocalAnnouncementState(
    val isLoading: Boolean = false,
    val courses: List<ClassEntity> = emptyList(),
    val selectedCourseId: String? = null,
    val announcements: List<Announcement> = emptyList(),
    val selectedAnnouncementId: String? = null,
)