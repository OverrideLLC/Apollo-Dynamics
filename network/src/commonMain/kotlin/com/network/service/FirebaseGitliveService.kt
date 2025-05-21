package com.network.service

import com.network.data.StatusCourse
import com.shared.utils.data.firebase.Attendance
import com.shared.utils.data.firebase.Course
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.firestore

class FirebaseGitliveService() {
    /**
     * Registra la asistencia de un estudiante a un curso mediante un código QR.
     *
     * @param courseId El ID del curso.
     * @param studentId El ID del estudiante.
     * @param timestamp El Timestamp de la sesión de asistencia.
     * @return Un String indicando el resultado de la operación:
     *         - "Éxito: Asistencia actualizada..." si la asistencia se registra correctamente.
     *         - "Info: El estudiante..." si el estudiante ya tiene la asistencia registrada o si el estado no es "NOT_ATTENDED".
     *         - "Error: ..." en caso de algún problema (curso no encontrado, estudiante no en la lista, etc.).
     */
    suspend fun qrAttendance(courseId: String, studentId: String, timestamp: Timestamp): String {
        return try {
            val courseRef = Firebase.firestore.collection("courses").document(courseId)
            val courseSnapshot = courseRef.get() // Obtener el DocumentSnapshot primero

            // Verificar si el curso existe y deserializarlo
            if (!courseSnapshot.exists) {
                return "Error: El curso con ID '$courseId' no fue encontrado."
            }
            val course = courseSnapshot.data(Course.serializer())
            val currentAttendanceMap = course.attendance ?: emptyMap()
            val attendanceListForTimestamp = currentAttendanceMap[timestamp]

            if (attendanceListForTimestamp == null) {
                return "Error: No se encontraron registros de asistencia para este timestamp en el curso '$courseId'."
            }

            val studentIndex = attendanceListForTimestamp.indexOfFirst { it.studentId == studentId }

            if (studentIndex == -1) {
                return "Error: Estudiante con ID '$studentId' no encontrado en la lista de asistencia para este timestamp."
            }

            val studentAttendance = attendanceListForTimestamp[studentIndex]

            when (studentAttendance.status) {
                StatusCourse.ATTENDED.status -> {
                    return "Info: El estudiante '$studentId' ya tiene registrada la asistencia."
                }

                StatusCourse.NOT_ATTENDED.status -> {
                    val updatedStudentAttendance =
                        studentAttendance.copy(status = StatusCourse.ATTENDED.status)

                    val newAttendanceListForTimestamp =
                        attendanceListForTimestamp.toMutableList().apply {
                            this[studentIndex] = updatedStudentAttendance
                        }.toList()

                    val newAttendanceMap = currentAttendanceMap.toMutableMap().apply {
                        this[timestamp] = newAttendanceListForTimestamp
                    }.toMap()

                    val updatedCourse = course.copy(attendance = newAttendanceMap)

                    courseRef.update(Course.serializer(), updatedCourse)

                    return "Éxito: Asistencia actualizada para el estudiante '$studentId'."
                }

                else -> {
                    return "Info: El estado de asistencia del estudiante '$studentId' es '${studentAttendance.status}', no se requiere actualización."
                }
            }
        } catch (e: Exception) {
            return "Error al actualizar asistencia: ${e.message}"
        }
    }

    /**
     * Agrega un nuevo curso a la colección "courses" en Firestore.
     *
     * @param newCourse El objeto Course que se va a agregar.
     * Asegúrate de que este objeto esté correctamente inicializado
     * con todos los datos necesarios para un nuevo curso.
     * El campo 'attendance' podría ser nulo o un mapa vacío inicialmente.
     * @return Un String indicando el resultado de la operación.
     * En caso de éxito, incluirá el ID del nuevo curso.
     */
    suspend fun addCourse(newCourse: Course): String {
        return try {
            val documentReference = Firebase.firestore.collection("courses")
                .add(Course.serializer(), newCourse)
            val newCourseId = documentReference.id

            "Éxito: Nuevo curso agregado con ID: $newCourseId"
        } catch (e: Exception) {
            "Error al agregar el nuevo curso: ${e.message}"
        }
    }

    /**
     * Añade una lista de estudiantes a todas las listas de asistencia existentes de un curso.
     * Los estudiantes se añaden con el estado StatusCourse.NOT_ATTENDED.
     * Si un estudiante ya existe en una lista de asistencia, no se duplica.
     *
     * @param courseId El ID del curso al que se añadirán los estudiantes.
     * @param newStudentIds Una lista de IDs de los estudiantes a añadir.
     * @return Un String indicando el resultado de la operación.
     */
    suspend fun addStudentsToCourse(courseId: String, newStudentIds: List<String>): String {
        if (newStudentIds.isEmpty()) {
            return "Info: No se proporcionaron IDs de estudiantes para añadir."
        }

        return try {
            val courseRef = Firebase.firestore.collection("courses").document(courseId)
            val courseSnapshot = courseRef.get()

            if (!courseSnapshot.exists) {
                return "Error: El curso con ID '$courseId' no fue encontrado."
            }
            val course = courseSnapshot.data(Course.serializer())

            // Si no hay mapa de asistencia, no hay listas a las que añadir estudiantes.
            val currentAttendanceMap =
                course.attendance // Guardamos en una variable local para el smart cast o manejo explícito
            if (currentAttendanceMap == null || currentAttendanceMap.isEmpty()) {
                return "Info: El curso '$courseId' no tiene sesiones de asistencia existentes. No se añadieron estudiantes a ninguna sesión."
            }

            // CORRECCIÓN AQUÍ:
            // Creamos una copia mutable del mapa de asistencia para modificarlo.
            // Usamos el operador de aserción no nula (!!) después de la verificación,
            // o el operador elvis con toMutableMap() si aún fuera necesario (aunque la verificación anterior lo hace seguro).
            // Sin embargo, es más seguro trabajar con la variable local `currentAttendanceMap` que ya sabemos que no es nula aquí.
            val updatedAttendanceMap =
                currentAttendanceMap.toMutableMap() // Ahora esto es seguro debido a la verificación anterior.

            var studentsEffectivelyAddedToAnyList = false

            // Iteramos sobre cada entrada (timestamp y su lista de asistencia) en el mapa.
            // Usamos currentAttendanceMap aquí también por seguridad, aunque updatedAttendanceMap se está modificando.
            for ((timestamp, attendanceListForTimestamp) in currentAttendanceMap) {
                val mutableAttendanceListForTimestamp = attendanceListForTimestamp.toMutableList()
                var listModifiedThisIteration = false

                for (studentId in newStudentIds) {
                    val studentExists =
                        mutableAttendanceListForTimestamp.any { it.studentId == studentId }
                    if (!studentExists) {
                        mutableAttendanceListForTimestamp.add(
                            Attendance(
                                studentId = studentId,
                                status = StatusCourse.NOT_ATTENDED.status
                            )
                        )
                        listModifiedThisIteration = true
                    }
                }

                if (listModifiedThisIteration) {
                    updatedAttendanceMap[timestamp] =
                        mutableAttendanceListForTimestamp.toList() // Convertir a inmutable
                    studentsEffectivelyAddedToAnyList = true
                }
            }

            if (!studentsEffectivelyAddedToAnyList && newStudentIds.isNotEmpty()) {
                return "Info: Todos los estudiantes proporcionados ya existen en todas las sesiones de asistencia del curso '$courseId' o el curso no tiene sesiones."
            }

            // Solo actualizamos si realmente se hicieron cambios
            if (studentsEffectivelyAddedToAnyList) {
                val updatedCourse =
                    course.copy(attendance = updatedAttendanceMap.toMap()) // Convertir a inmutable
                courseRef.update(Course.serializer(), updatedCourse)
                return "Éxito: Estudiantes procesados para el curso '$courseId'. Verifique las listas de asistencia."
            } else {
                // Esto cubre el caso donde los estudiantes ya existían o no hubo sesiones a las que añadir.
                // El mensaje de retorno más específico ya se dio antes.
                return "Info: No se realizaron modificaciones en las listas de asistencia del curso '$courseId'."
            }

        } catch (e: Exception) {
            // Log.e("FirebaseGitliveService", "Error al añadir estudiantes al curso $courseId", e)
            "Error al añadir estudiantes al curso: ${e.message}"
        }
    }

    /**
     * Obtiene todos los cursos de la colección "courses" en Firestore.
     *
     * @return Result<List<Course>> que contiene la lista de cursos si es exitoso,
     * o una excepción si ocurre un error.
     */
    suspend fun getAllCourses(): Result<List<Course>> {
        return try {
            val querySnapshot = Firebase.firestore.collection("courses").get()
            val courses = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data(Course.serializer())
            }
            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
