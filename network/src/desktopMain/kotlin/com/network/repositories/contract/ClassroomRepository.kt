package com.network.repositories.contract

import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course

interface ClassroomRepository {
    suspend fun getCourses(): List<Course>
    suspend fun getAnnouncements(courseId: String): List<Announcement>
}