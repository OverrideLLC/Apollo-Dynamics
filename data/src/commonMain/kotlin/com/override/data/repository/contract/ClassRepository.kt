package com.override.data.repository.contract

import com.override.data.utils.data.ClassData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

// --- Interfaz ---
interface ClassRepository {
    // Obtiene un flujo de todas las clases, mapeadas a ClassData (incluyendo roster y historial)
    fun getAllClasses(): Flow<List<ClassData>>

    // Obtiene una clase específica por ID
    suspend fun getClassById(id: String): ClassData?

    // Añade o actualiza una clase (incluyendo su roster inicial)
    suspend fun addOrUpdateClass(classData: ClassData)

    // Borra una clase por ID
    suspend fun deleteClass(classId: String)

    // Añade un estudiante a una clase existente
    suspend fun addStudentToClass(classId: String, studentId: String)

    // Remueve un estudiante de una clase
    suspend fun removeStudentFromClass(classId: String, studentId: String)

    // Obtiene solo las fechas en las que se pasó lista para una clase
    suspend fun getAttendanceDatesForClass(classId: String): List<LocalDate>
}

