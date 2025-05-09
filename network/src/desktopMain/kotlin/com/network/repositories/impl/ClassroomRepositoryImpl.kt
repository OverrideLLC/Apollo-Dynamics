package com.network.repositories.impl

import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.Classroom.Courses.Students
import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.Student
import com.network.repositories.contract.ClassroomRepository
import com.network.services.ClassroomServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

class ClassroomRepositoryImpl(
    private val classroomServices: ClassroomServices,
) : ClassroomRepository {
    private val serviceScope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)


    /**
     * Initializes the underlying Classroom client in the repository.
     * It's recommended to call this once at application startup or before the first API call
     * to handle any potential authorization or setup issues proactively.
     * This operation is performed asynchronously on the [serviceScope].
     *
     * @param onComplete Optional callback that receives a Boolean indicating success (true) or failure (false).
     */
    override fun initialize(onComplete: ((Boolean) -> Unit)?) {
        serviceScope.launch {
            try {
                classroomServices.initializeClient()
                println("ClassroomServices: Repository client initialized successfully.")
                onComplete?.invoke(true)
            } catch (e: Exception) {
                println("ClassroomServices: Error initializing repository client - ${e.message}")
                e.printStackTrace()
                onComplete?.invoke(false)
            }
        }
    }

    /**
     * Fetches the list of courses.
     * @return A list of [Course] objects, or an empty list if an error occurs.
     */
    override suspend fun getAllCourses(): List<Course> {
        return try {
            withContext(Dispatchers.Swing) { // Ensure repository call is on IO dispatcher
                classroomServices.getCourses()
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error fetching courses - ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Creates a new course.
     * @param courseName The name for the new course.
     */
    override suspend fun createCourse(courseName: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.addCourse(courseName)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error creating course '$courseName' - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Updates an existing course's name.
     * @param courseId The ID of the course to update.
     * @param newName The new name for the course.
     */
    override suspend fun renameCourse(courseId: String, newName: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.updateCourse(courseId, newName)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error renaming course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Archives a course (Classroom API's equivalent of deleting).
     * @param courseId The ID of the course to archive.
     */
    override suspend fun archiveCourse(courseId: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.deleteCourse(courseId)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error archiving course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Fetches the list of student identifiers for a specific course.
     * @param courseId The ID of the course.
     * @return A list of student identifiers (emails or IDs), or an empty list on error.
     */
    override suspend fun getCourseStudents(courseId: String): List<Student> {
        return try {
            withContext(Dispatchers.Swing) {
                classroomServices.getStudents(courseId)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error fetching students for course $courseId - ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Adds a student to a course.
     * @param courseId The ID of the course.
     * @param studentEmail The email of the student to add.
     */
    override suspend fun enrollStudent(courseId: String, studentEmail: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.addStudent(courseId, studentEmail)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error enrolling student $studentEmail in course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Removes a student from a course.
     * @param courseId The ID of the course.
     * @param studentEmail The email (or ID) of the student to remove.
     */
    override suspend fun unenrollStudent(courseId: String, studentEmail: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.removeStudent(courseId, studentEmail)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error unenrolling student $studentEmail from course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Fetches announcements for a specific course.
     * @param courseId The ID of the course.
     * @return A list of [Announcement] objects, or an empty list on error.
     */
    override suspend fun getCourseAnnouncements(courseId: String): List<Announcement> {
        return try {
            withContext(Dispatchers.Swing) {
                classroomServices.getAnnouncements(courseId)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error fetching announcements for course $courseId - ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Creates a new announcement in a course.
     * @param courseId The ID of the course.
     * @param content The text content of the announcement.
     */
    override suspend fun postAnnouncement(courseId: String, content: String) {
        try {
            withContext(Dispatchers.Default) {
                classroomServices.addAnnouncement(courseId, content)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error posting announcement to course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Updates an existing announcement in a course.
     * @param courseId The ID of the course.
     * @param announcementId The ID of the announcement to update.
     * @param newContent The new text content for the announcement.
     */
    override suspend fun editAnnouncement(
        courseId: String,
        announcementId: String,
        newContent: String
    ) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.updateAnnouncement(courseId, announcementId, newContent)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error editing announcement $announcementId in course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Deletes an announcement from a course.
     * @param courseId The ID of the course.
     * @param announcementId The ID of the announcement to delete.
     */
    override suspend fun removeAnnouncement(courseId: String, announcementId: String) {
        try {
            withContext(Dispatchers.Swing) {
                classroomServices.deleteAnnouncement(courseId, announcementId)
            }
        } catch (e: Exception) {
            println("ClassroomServices: Error removing announcement $announcementId from course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }
}
