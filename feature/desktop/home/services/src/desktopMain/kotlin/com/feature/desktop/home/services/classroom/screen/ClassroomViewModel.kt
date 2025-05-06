package com.feature.desktop.home.services.classroom.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.services.classroom.model.Course // Required import
import com.network.repositories.contract.ClassroomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing

class ClassroomViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ClassroomState())
    val state = _state.asStateFlow()

    init {
        getCourses()
    }

    fun update(update: ClassroomState.() -> ClassroomState) {
        _state.value = update(_state.value)
    }

    // Called when a course is selected from the UI
    fun onCourseSelected(course: Course) {
        update {
            // If the same course is clicked again, it remains selected.
            // To toggle selection (deselect if same course clicked), you could add:
            // if (selectedCourseId == course.id) {
            //     copy(selectedCourseId = null, servicesVisible = false)
            // } else {
            //     copy(selectedCourseId = course.id, servicesVisible = true)
            // }
            copy(selectedCourseId = course.id, servicesVisible = true)
        }
        // Here you could also trigger loading data specific to the selected course if needed
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

    fun initialize() {
        viewModelScope.launch(Dispatchers.Swing) {
            classroomRepository.initialize()
        }
    }
}
