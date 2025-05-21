package com.override.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.override.data.entity.ClassEntity
import com.override.data.entity.ClassStudentCrossRef
import com.override.data.utils.data.ClassWithStudents
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassStudentCrossRef(crossRef: ClassStudentCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassStudentCrossRefs(crossRefs: List<ClassStudentCrossRef>)


    @Query("SELECT * FROM classes WHERE class_id = :id LIMIT 1")
    suspend fun getClassById(id: String): ClassEntity?

    @Query("SELECT * FROM classes ORDER BY name ASC")
    suspend fun getAllClasses(): List<ClassEntity>

    @Transaction
    @Query("SELECT * FROM classes WHERE class_id = :id")
    suspend fun getClassWithStudents(id: String): ClassWithStudents?

    @Transaction
    @Query("SELECT * FROM classes ORDER BY name ASC")
    suspend fun getAllClassesWithStudents(): List<ClassWithStudents>

    @Transaction
    @Query("SELECT * FROM classes ORDER BY name ASC")
    fun getAllClassesWithStudentsFlow(): Flow<List<ClassWithStudents>>

    @Query("DELETE FROM classes WHERE class_id = :id")
    suspend fun deleteClassById(id: String)

    @Query("DELETE FROM class_student_cross_ref WHERE class_id = :classId AND student_id = :studentId")
    suspend fun deleteStudentFromClass(classId: String, studentId: String)
}