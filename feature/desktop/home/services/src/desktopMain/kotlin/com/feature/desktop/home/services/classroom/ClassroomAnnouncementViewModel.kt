package com.feature.desktop.home.services.classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.network.repositories.contract.ClassroomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing

class ClassroomAnnouncementViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ClassroomAnnouncementState())
    val state = _state.asStateFlow()

    init {
        getCourses()
    }

    fun update(update: ClassroomAnnouncementState.() -> ClassroomAnnouncementState) {
        _state.value = update(_state.value)
    }

    fun onCourseSelected(course: Course) {
        update {
            copy(selectedCourseId = course.id, servicesVisible = true)
        }
    }

    fun addAnnouncement(content: String, courseId: String) {
        // This action will now be available when services are visible,
        // and the courseId will be the currently selected one.
        viewModelScope.launch(Dispatchers.Swing) {
            classroomRepository.postAnnouncement(courseId, content)
        }
    }

    fun getCourses() {
        viewModelScope.launch(Dispatchers.Swing) {
            val courses = classroomRepository.getAllCourses()
            // When courses are refreshed, reset the selection and hide services
            update { copy(courses = courses, selectedCourseId = null, servicesVisible = false) }
        }
    }

    fun removeAllAnnouncements(courseId: String) {
        viewModelScope.launch(Dispatchers.Swing) {
            val courseId = state.value.selectedCourseId ?: return@launch
            classroomRepository.getCourseAnnouncements(courseId).forEach {
                classroomRepository.removeAnnouncement(courseId, it.id)
            }
        }
    }

    fun removeAnnouncement(courseId: String, announcementId: String) {
        viewModelScope.launch(Dispatchers.Swing) {
            classroomRepository.removeAnnouncement(courseId, announcementId)
        }
    }

    fun updateAnnouncement(courseId: String, announcementId: String, content: String) {
        viewModelScope.launch(Dispatchers.Swing) {
            classroomRepository.editAnnouncement(
                courseId = courseId,
                announcementId = announcementId,
                newContent = content,
            )
        }
    }

    fun getCourseAnnouncements(courseId: String) {
        viewModelScope.launch(Dispatchers.Swing) {
            val announcements = classroomRepository.getCourseAnnouncements(courseId)
            update { copy(announcements = announcements) }
        }
    }

    fun onAnnouncementSelected(announcement: Announcement) {
        update { copy(selectedAnnouncement = announcement) }
    }
}