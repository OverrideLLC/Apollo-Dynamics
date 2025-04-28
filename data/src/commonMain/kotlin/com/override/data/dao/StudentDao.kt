package com.override.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.override.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow // Opcional si quieres observar cambios

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>) // Para insertar varios

    @Query("SELECT * FROM students WHERE student_id = :id LIMIT 1")
    suspend fun getStudentById(id: String): StudentEntity?

    @Query("SELECT * FROM students ORDER BY name ASC")
    suspend fun getAllStudents(): List<StudentEntity>

    // Opcional: Observar cambios en la lista de estudiantes
    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudentsFlow(): Flow<List<StudentEntity>>

     @Query("DELETE FROM students WHERE student_id = :id")
     suspend fun deleteStudentById(id: String)
}