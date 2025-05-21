package com.override.data.repository.contract
import com.override.data.utils.data.Student
import kotlinx.coroutines.flow.Flow

// --- Interfaz ---
interface StudentRepository {
    // Obtiene un flujo de todos los estudiantes, mapeados a tu clase de datos Student
    fun getAllStudents(): Flow<List<Student>>

    // Obtiene un estudiante específico por ID
    suspend fun getStudentById(id: String): Student?

    // Añade o actualiza un estudiante
    suspend fun addOrUpdateStudent(student: Student)

    // Añade o actualiza una lista de estudiantes
    suspend fun addOrUpdateStudents(students: List<Student>)

    // Borra un estudiante por ID
    suspend fun deleteStudent(studentId: String)
}