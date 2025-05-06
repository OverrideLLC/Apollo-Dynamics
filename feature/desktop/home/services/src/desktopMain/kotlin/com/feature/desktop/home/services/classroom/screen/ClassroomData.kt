package com.feature.desktop.home.services.classroom.screen

import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.Student

data class ClassroomData(
    val courses: List<Course>? = null,
    val students: List<Student>? = null,
    val announcements: List<Announcement>? = null
)
