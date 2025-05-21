package com.network.repository

import com.network.interfaces.FirebaseGitliveRepository
import com.network.service.FirebaseGitliveService
import com.shared.utils.data.firebase.Course
import dev.gitlive.firebase.firestore.Timestamp

class FirebaseGitliveRepositoryImpl(
    private val firebaseGitliveService: FirebaseGitliveService
): FirebaseGitliveRepository {
    override suspend fun qrAttendance(
        courseId: String,
        studentId: String,
        timestamp: Timestamp
    ): String {
        return firebaseGitliveService.qrAttendance(courseId, studentId, timestamp)
    }

    override suspend fun addCourse(newCourse: Course): String {
        return firebaseGitliveService.addCourse(newCourse)
    }

    override suspend fun addStudentsToCourse(
        courseId: String,
        newStudentIds: List<String>
    ): String {
        return firebaseGitliveService.addStudentsToCourse(courseId, newStudentIds)
    }

    override suspend fun getAllCourses(): Result<List<Course>>{
        return firebaseGitliveService.getAllCourses()
    }
}