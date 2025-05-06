package com.network.services

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.ClassroomScopes
import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.Student
import com.network.utils.constants.Constants
import com.shared.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class ClassroomServices(
    private val jsonFactory: GsonFactory,
    private val httpTransport: NetHttpTransport,
) {
    private val applicationName: String = "Apollo"

    // Scopes required for Classroom API operations
    private val classroomScopes = listOf(
        ClassroomScopes.CLASSROOM_COURSES,          // View and manage courses
        ClassroomScopes.CLASSROOM_ROSTERS,          // View and manage course rosters (students, teachers)
        ClassroomScopes.CLASSROOM_ANNOUNCEMENTS,    // View and manage announcements
        ClassroomScopes.CLASSROOM_COURSEWORK_ME,    // Manage coursework for the authenticated user
        ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS, // Manage coursework for students
        ClassroomScopes.CLASSROOM_PROFILE_EMAILS,   // View user profile emails
        ClassroomScopes.CLASSROOM_PROFILE_PHOTOS    // View user profile photos
        // Add other scopes if needed, e.g., for topics, materials, etc.
    )

    // Lazily initialized Classroom service client
    private val classroomService: Classroom by lazy {
        runCatching {
            // runBlocking is used here because lazy initialization is not a suspend context.
            // This will block the thread calling it for the first time until credentials are fetched.
            // Ensure this is handled appropriately (e.g., by calling initializeClient() from a coroutine).
            val credential = runBlocking(Dispatchers.Swing) { getCredentials(classroomScopes) }
            Classroom.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build()
        }.getOrElse {
            // Log the error or handle it more gracefully
            println("FATAL: Failed to initialize Classroom service in lazy block: ${it.message}")
            it.printStackTrace()
            throw IllegalStateException("Failed to initialize Classroom service", it)
        }
    }

    /**
     * Retrieves authorized credentials for accessing Google Classroom API.
     * This is a suspend function as it involves I/O operations (reading resource, network auth).
     * @param scopes A collection of OAuth scopes required.
     * @return An authorized [Credential] object.
     * @throws IOException If there's an issue reading resources or during the authorization process.
     */
    @OptIn(ExperimentalResourceApi::class)
    @Throws(IOException::class)
    private suspend fun getCredentials(scopes: Collection<String>): Credential =
        withContext(Dispatchers.Swing) {
            val clientSecretsInputStream = try {
                // Assuming Res.readBytes provides the content of the client_secret.json file
                // Ensure Constants.CREDENTIALS_RESOURCE_PATH_CLASSROOM points to your client_secret.json in resources
                Res.readBytes(Constants.CREDENTIALS_RESOURCE_PATH_CLASSROOM).inputStream()
            } catch (e: Exception) {
                throw IOException(
                    "Failed to read credentials resource: ${Constants.CREDENTIALS_RESOURCE_PATH_CLASSROOM}",
                    e
                )
            }

            val clientSecrets = clientSecretsInputStream.use { stream ->
                GoogleClientSecrets.load(jsonFactory, InputStreamReader(stream))
            }

            // Ensure Constants.TOKENS_DIRECTORY_PATH is a valid, writable path for storing OAuth tokens
            val dataStoreFactory = FileDataStoreFactory(File(Constants.TOKENS_DIRECTORY_PATH))

            val flow = GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientSecrets,
                scopes
            )
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline") // "offline" to get a refresh token
                .build()

            // LocalServerReceiver will start a local server to handle the OAuth redirect
            // Ensure port 8888 (or your chosen port) is available
            val receiver = LocalServerReceiver.Builder().setPort(Constants.OAUTH_REDIRECT_PORT)
                .build() // Example: Constants.OAUTH_REDIRECT_PORT = 8888

            println("ClassroomRepositoryImpl: Attempting to authorize user (this may open a browser window)...")
            try {
                // "user" is a generic ID for the local credential store.
                AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Authorization failed - ${e.message}")
                throw e // Re-throw to be handled by the caller or logged
            } finally {
                println("ClassroomRepositoryImpl: Authorization attempt finished.")
            }
        }

    /**
     * Explicitly initializes the Classroom client.
     * Call this from a coroutine at app startup or before the first API call
     * to handle potential initialization errors gracefully.
     */
    suspend fun initializeClient() {
        withContext(Dispatchers.Swing) {
            try {
                // Accessing classroomService here will trigger its lazy initialization
                // Perform a lightweight operation to confirm connectivity, e.g., listing courses with pageSize 1
                classroomService.courses().list().setPageSize(20).execute()
                println("ClassroomRepositoryImpl: Client initialized successfully.")
            } catch (e: Exception) {
                println("ClassroomRepositoryImpl: Client initialization failed - ${e.message}")
                e.printStackTrace()
                // Optionally re-throw or handle as a critical error
                throw IllegalStateException(
                    "Failed to initialize Classroom client during explicit initialization",
                    e
                )
            }
        }
    }

    suspend fun getCourses(): List<Course> = withContext(Dispatchers.Swing) {
        try {
            val response = classroomService.courses().list()
                .setPageSize(Constants.API_PAGE_SIZE) // Example: Constants.API_PAGE_SIZE = 20
                .execute()
            response.courses ?: emptyList()
        } catch (e: IOException) {
            println("ClassroomRepositoryImpl: Error getting courses - ${e.message}")
            e.printStackTrace()
            emptyList() // Return an empty list on error
        }
    }

    suspend fun updateCourse(courseId: String, updatedName: String) =
        withContext(Dispatchers.Swing) {
            try {
                val courseToUpdate = classroomService.courses().get(courseId).execute()
                if (courseToUpdate != null) {
                    courseToUpdate.name = updatedName
                    // To update only specific fields, use an updateMask.
                    // For example, to only update the name:
                    // classroomService.courses().update(courseId, courseToUpdate).setUpdateMask("name").execute()
                    classroomService.courses().update(courseId, courseToUpdate).execute()
                    println("ClassroomRepositoryImpl: Course $courseId updated successfully.")
                } else {
                    println("ClassroomRepositoryImpl: Course $courseId not found for update.")
                }
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error updating course $courseId - ${e.message}")
                e.printStackTrace()
            }
        }

    suspend fun deleteCourse(courseId: String) = withContext(Dispatchers.Swing) {
        try {
            // Classroom API typically archives courses instead of outright deleting them.
            // To "delete" a course, you change its state to ARCHIVED.
            val course = classroomService.courses().get(courseId).execute()
            if (course != null) {
                course.courseState = "ARCHIVED"
                classroomService.courses().update(courseId, course).set(
                    "courseState",
                    course.courseState
                ).execute()
                println("ClassroomRepositoryImpl: Course $courseId archived successfully.")
            } else {
                println("ClassroomRepositoryImpl: Course $courseId not found for archiving.")
            }
        } catch (e: IOException) {
            println("ClassroomRepositoryImpl: Error archiving course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun addCourse(courseName: String) = withContext(Dispatchers.Swing) {
        try {
            val newCourse = Course().apply {
                name = courseName
                ownerId = "me" // The authenticated user becomes the owner
                courseState = "ACTIVE" // Default state for a new course
                // You can set other properties like description, section, room, etc.
                // descriptionHeading = "Course Description"
                // section = "Fall Semester"
            }
            val createdCourse = classroomService.courses().create(newCourse).execute()
            println("ClassroomRepositoryImpl: Course created successfully with ID: ${createdCourse.id}, Name: ${createdCourse.name}")
            // Optionally return the createdCourse object or its ID
        } catch (e: IOException) {
            println("ClassroomRepositoryImpl: Error adding course '$courseName' - ${e.message}")
            e.printStackTrace()
            // Consider re-throwing or returning a result object to indicate failure
        }
    }

    suspend fun getStudents(courseId: String): List<String> =
        withContext(Dispatchers.Swing) {
            try {
                val response = classroomService.courses().students().list(courseId)
                    .setPageSize(Constants.API_STUDENT_PAGE_SIZE) // Example: Constants.API_STUDENT_PAGE_SIZE = 100
                    .execute()
                // Extracts email addresses if available, otherwise falls back to userId.
                response.students?.mapNotNull { it.profile?.emailAddress ?: it.userId }
                    ?: emptyList()
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error getting students for course $courseId - ${e.message}")
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun addStudent(courseId: String, studentEmail: String) =
        withContext(Dispatchers.Swing) {
            try {
                // The student's email is used as their ID for enrollment.
                val student = Student().setUserId(studentEmail)
                classroomService.courses().students().create(courseId, student).execute()
                println("ClassroomRepositoryImpl: Student $studentEmail added to course $courseId successfully.")
            } catch (e: IOException) {
                // Handle specific errors, e.g., student already exists or invalid email
                println("ClassroomRepositoryImpl: Error adding student $studentEmail to course $courseId - ${e.message}")
                e.printStackTrace()
            }
        }

    suspend fun removeStudent(courseId: String, studentEmail: String) =
        withContext(Dispatchers.Swing) {
            try {
                // The student's email (or their Google ID if known) is used for removal.
                classroomService.courses().students().delete(courseId, studentEmail).execute()
                println("ClassroomRepositoryImpl: Student $studentEmail removed from course $courseId successfully.")
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error removing student $studentEmail from course $courseId - ${e.message}")
                e.printStackTrace()
            }
        }

    suspend fun updateStudent(
        courseId: String,
        studentEmail: String,
        updatedName: String
    ) = withContext(Dispatchers.Swing) {
        // Direct update of student profile information (like name) is not supported via the Classroom API.
        // Student profiles are managed through Google Accounts or Google Workspace Admin SDK.
        println("ClassroomRepositoryImpl: Updating student profile information (e.g., name) for $studentEmail in course $courseId is not directly supported by the Classroom API. Name provided: '$updatedName'.")
        // If there were other updatable student properties specific to the course enrollment, they would be handled here.
    }

    suspend fun getAnnouncements(courseId: String): List<Announcement> =
        withContext(Dispatchers.Swing) {
            try {
                val response = classroomService.courses().announcements().list(courseId)
                    .setPageSize(Constants.API_PAGE_SIZE)
                    .setOrderBy("updateTime desc") // Get newest announcements first
                    .execute()
                response.announcements ?: emptyList()
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error getting announcements for course $courseId - ${e.message}")
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun updateAnnouncement(
        courseId: String,
        announcementId: String,
        updatedContent: String
    ) = withContext(Dispatchers.Swing) {
        try {
            // First, get the existing announcement to ensure it exists and to use its current state as a base.
            val announcement =
                classroomService.courses().announcements().get(courseId, announcementId).execute()
            if (announcement != null) {
                announcement.text = updatedContent // Update the text content
                // To update only specific fields, use an updateMask.
                // For example, to only update the text:
                // .setUpdateMask("text")
                classroomService.courses().announcements()
                    .patch(courseId, announcementId, announcement)
                    .setUpdateMask("text") // Crucial to specify what field is being updated
                    .execute()
                println("ClassroomRepositoryImpl: Announcement $announcementId in course $courseId updated successfully.")
            } else {
                println("ClassroomRepositoryImpl: Announcement $announcementId not found in course $courseId for update.")
            }
        } catch (e: IOException) {
            println("ClassroomRepositoryImpl: Error updating announcement $announcementId in course $courseId - ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun deleteAnnouncement(courseId: String, announcementId: String) =
        withContext(Dispatchers.Swing) {
            try {
                classroomService.courses().announcements().delete(courseId, announcementId)
                    .execute()
                println("ClassroomRepositoryImpl: Announcement $announcementId in course $courseId deleted successfully.")
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error deleting announcement $announcementId in course $courseId - ${e.message}")
                e.printStackTrace()
            }
        }

    suspend fun addAnnouncement(courseId: String, content: String) =
        withContext(Dispatchers.Swing) {
            try {
                val newAnnouncement = Announcement().apply {
                    text = content
                    // state = "PUBLISHED" // Announcements are typically published by default.
                    // assigneeMode = "ALL_STUDENTS" // Default, can be changed if targeting specific students (more complex)
                }
                val createdAnnouncement =
                    classroomService.courses().announcements().create(courseId, newAnnouncement)
                        .execute()
                println("ClassroomRepositoryImpl: Announcement added to course $courseId successfully with ID: ${createdAnnouncement.id}")
                // Optionally return the createdAnnouncement object or its ID
            } catch (e: IOException) {
                println("ClassroomRepositoryImpl: Error adding announcement to course $courseId - ${e.message}")
                e.printStackTrace()
            }
        }
}
