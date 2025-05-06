package com.network.repositories.contract

import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

/**
 * Interface defining the contract for Classroom operations.
 */
interface ClassroomRepository {

    /**
     * Initializes the underlying Classroom client in the repository.
     * It's recommended to call this once at application startup or before the first API call
     * to handle any potential authorization or setup issues proactively.
     * This operation is performed asynchronously on the [serviceScope].
     *
     * @param onComplete Optional callback that receives a Boolean indicating success (true) or failure (false).
     */
    fun initialize(onComplete: ((Boolean) -> Unit)? = null)

    /**
     * Fetches the list of courses.
     * @return A list of [Course] objects, or an empty list if an error occurs.
     */
    suspend fun getAllCourses(): List<Course>

    /**
     * Creates a new course.
     * @param courseName The name for the new course.
     */
    suspend fun createCourse(courseName: String)

    /**
     * Updates an existing course's name.
     * @param courseId The ID of the course to update.
     * @param newName The new name for the course.
     */
    suspend fun renameCourse(courseId: String, newName: String)

    /**
     * Archives a course (Classroom API's equivalent of deleting).
     * @param courseId The ID of the course to archive.
     */
    suspend fun archiveCourse(courseId: String)

    /**
     * Fetches the list of student identifiers for a specific course.
     * @param courseId The ID of the course.
     * @return A list of student identifiers (emails or IDs), or an empty list on error.
     */
    suspend fun getCourseStudents(courseId: String): List<String>

    /**
     * Adds a student to a course.
     * @param courseId The ID of the course.
     * @param studentEmail The email of the student to add.
     */
    suspend fun enrollStudent(courseId: String, studentEmail: String)

    /**
     * Removes a student from a course.
     * @param courseId The ID of the course.
     * @param studentEmail The email (or ID) of the student to remove.
     */
    suspend fun unenrollStudent(courseId: String, studentEmail: String)

    /**
     * Fetches announcements for a specific course.
     * @param courseId The ID of the course.
     * @return A list of [Announcement] objects, or an empty list on error.
     */
    suspend fun getCourseAnnouncements(courseId: String): List<Announcement>

    /**
     * Creates a new announcement in a course.
     * @param courseId The ID of the course.
     * @param content The text content of the announcement.
     */
    suspend fun postAnnouncement(courseId: String, content: String)

    /**
     * Updates an existing announcement in a course.
     * @param courseId The ID of the course.
     * @param announcementId The ID of the announcement to update.
     * @param newContent The new text content for the announcement.
     */
    suspend fun editAnnouncement(courseId: String, announcementId: String, newContent: String)

    /**
     * Deletes an announcement from a course.
     * @param courseId The ID of the course.
     * @param announcementId The ID of the announcement to delete.
     */
    suspend fun removeAnnouncement(courseId: String, announcementId: String)
}
