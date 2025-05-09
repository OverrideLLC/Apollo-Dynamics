package com.network.repositories.contract

import com.google.api.services.classroom.model.Announcement
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.CourseWork
import com.google.api.services.classroom.model.Student
import com.google.api.services.classroom.model.StudentSubmission

interface ClassroomRepository {

    /**
     * Initializes the underlying Classroom client in the repository.
     * It's recommended to call this once at application startup or before the first API call
     * to handle any potential authorization or setup issues proactively.
     * This operation is performed asynchronously.
     *
     * Inicializa el cliente de Classroom subyacente en el repositorio.
     * Se recomienda llamar a esto una vez al iniciar la aplicación o antes de la primera llamada a la API
     * para manejar de manera proactiva cualquier posible problema de autorización o configuración.
     * Esta operación se realiza de forma asíncrona.
     *
     * @param onComplete Optional callback that receives a Boolean indicating success (true) or failure (false).
     * Callback opcional que recibe un Booleano indicando éxito (true) o fallo (false).
     */
    fun initialize(onComplete: ((Boolean) -> Unit)?)

    /**
     * Fetches the list of courses.
     * Obtiene la lista de cursos.
     *
     * @return A list of [Course] objects, or an empty list if an error occurs.
     * Una lista de objetos [Course], o una lista vacía si ocurre un error.
     */
    suspend fun getAllCourses(): List<Course>

    /**
     * Creates a new course.
     * Crea un nuevo curso.
     *
     * @param courseName The name for the new course.
     * El nombre para el nuevo curso.
     */
    suspend fun createCourse(courseName: String)

    /**
     * Updates an existing course's name.
     * Actualiza el nombre de un curso existente.
     *
     * @param courseId The ID of the course to update.
     * El ID del curso a actualizar.
     * @param newName The new name for the course.
     * El nuevo nombre para el curso.
     */
    suspend fun renameCourse(courseId: String, newName: String)

    /**
     * Archives a course (Classroom API's equivalent of deleting).
     * Archiva un curso (equivalente a eliminar en la API de Classroom).
     *
     * @param courseId The ID of the course to archive.
     * El ID del curso a archivar.
     */
    suspend fun archiveCourse(courseId: String)

    /**
     * Fetches the list of student identifiers for a specific course.
     * Obtiene la lista de identificadores de estudiantes para un curso específico.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @return A list of [Student] objects, or an empty list on error.
     * Una lista de objetos [Student], o una lista vacía en caso de error.
     */
    suspend fun getCourseStudents(courseId: String): List<Student>

    /**
     * Adds a student to a course.
     * Añade un estudiante a un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param studentEmail The email of the student to add.
     * El correo electrónico del estudiante a añadir.
     */
    suspend fun enrollStudent(courseId: String, studentEmail: String)

    /**
     * Removes a student from a course.
     * Elimina un estudiante de un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param studentEmail The email (or ID) of the student to remove.
     * El correo electrónico (o ID) del estudiante a eliminar.
     */
    suspend fun unenrollStudent(courseId: String, studentEmail: String)

    /**
     * Fetches announcements for a specific course.
     * Obtiene los anuncios para un curso específico.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @return A list of [Announcement] objects, or an empty list on error.
     * Una lista de objetos [Announcement], o una lista vacía en caso de error.
     */
    suspend fun getCourseAnnouncements(courseId: String): List<Announcement>

    /**
     * Creates a new announcement in a course.
     * Crea un nuevo anuncio en un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param content The text content of the announcement.
     * El contenido de texto del anuncio.
     */
    suspend fun postAnnouncement(courseId: String, content: String)

    /**
     * Updates an existing announcement in a course.
     * Actualiza un anuncio existente en un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param announcementId The ID of the announcement to update.
     * El ID del anuncio a actualizar.
     * @param newContent The new text content for the announcement.
     * El nuevo contenido de texto para el anuncio.
     */
    suspend fun editAnnouncement(
        courseId: String,
        announcementId: String,
        newContent: String
    )

    /**
     * Deletes an announcement from a course.
     * Elimina un anuncio de un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param announcementId The ID of the announcement to delete.
     */
    suspend fun removeAnnouncement(courseId: String, announcementId: String)

    /**
     * Fetches the list of coursework for a specific course.
     * Obtiene la lista de trabajos de clase para un curso específico.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @return A list of [CourseWork] objects, or an empty list on error.
     * Una lista de objetos [CourseWork], o una lista vacía en caso de error.
     */
    suspend fun getCourseWork(courseId: String): List<CourseWork>

    /**
     * Fetches the list of student submissions for a specific coursework item.
     * Obtiene la lista de entregas de estudiantes para un elemento de trabajo de clase específico.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param courseworkId The ID of the coursework item.
     * El ID del elemento de trabajo de clase.
     * @return A list of [StudentSubmission] objects, or an empty list on error.
     * Una lista de objetos [StudentSubmission], o una lista vacía en caso de error.
     */
    suspend fun getStudentSubmissions(
        courseId: String,
        courseworkId: String
    ): List<StudentSubmission>

    /**
     * Creates a new coursework item in a course.
     * Crea un nuevo elemento de trabajo de clase en un curso.
     *
     * @param courseId The ID of the course.
     * El ID del curso.
     * @param coursework The [CourseWork] object containing the details of the assignment.
     * El objeto [CourseWork] que contiene los detalles de la tarea.
     * @return The created [CourseWork] object, or null if an error occurs.
     * El objeto [CourseWork] creado, o null si ocurre un error.
     */
    suspend fun createCourseWork(courseId: String, coursework: CourseWork): CourseWork?
}
