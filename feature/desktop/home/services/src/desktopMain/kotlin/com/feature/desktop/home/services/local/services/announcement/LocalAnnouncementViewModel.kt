package com.feature.desktop.home.services.local.services.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.services.classroom.model.Announcement
import com.override.data.entity.ClassEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class LocalAnnouncementViewModel : ViewModel() {

    private val _state = MutableStateFlow(LocalAnnouncementState())
    val state = _state.asStateFlow()

    fun onAction(action: LocalAnnouncementAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    fun onCourseSelected(course: ClassEntity) {
        _state.value = _state.value.copy(
            selectedCourseId = course.id
        )
    }

    fun removeAnnouncement(announcementId: String, courseId: String) {
    }

    fun onAnnouncementSelected(announcement: Announcement) {

    }
}