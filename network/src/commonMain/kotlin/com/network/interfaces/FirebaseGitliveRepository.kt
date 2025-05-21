package com.network.interfaces

import com.shared.utils.data.firebase.Course
import dev.gitlive.firebase.firestore.Timestamp

interface FirebaseGitliveRepository {
    suspend fun qrAttendance(courseId: String, studentId: String, timestamp: Timestamp): String
    suspend fun addCourse(newCourse: Course): String
    suspend fun addStudentsToCourse(courseId: String, newStudentIds: List<String>): String
    suspend fun getAllCourses(): Result<List<Course>>
}