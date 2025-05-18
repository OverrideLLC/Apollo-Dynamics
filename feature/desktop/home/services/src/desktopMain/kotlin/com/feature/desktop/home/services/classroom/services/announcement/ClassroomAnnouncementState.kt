package com.feature.desktop.home.services.classroom.services.announcement

import com.feature.desktop.home.services.classroom.utils.ClassroomServices
import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.Student
import kotlin.enums.EnumEntries

data class ClassroomAnnouncementState(
    val courses: List<Course>? = null,
    val students: List<Student>? = null,
    val announcements: List<Announcement>? = null,
    val classroomServices: EnumEntries<ClassroomServices> = ClassroomServices.entries,
    val selectedCourseId: String? = null, // To track the selected course
    val servicesVisible: Boolean = false,   // To control visibility of services section
    val selectedAnnouncement: Announcement? = null,
)