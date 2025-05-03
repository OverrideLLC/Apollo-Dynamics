package com.override.data.utils.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.override.data.entity.ClassEntity
import com.override.data.entity.ClassStudentCrossRef
import com.override.data.entity.StudentEntity

data class ClassWithStudents(
    @Embedded val classEntity: ClassEntity,
    @Relation(
        parentColumn = "class_id", // Columna en ClassEntity (padre)
        entityColumn = "student_id", // Columna en StudentEntity (hijo)
        associateBy = Junction(ClassStudentCrossRef::class) // Tabla de unión
    )
    val students: List<StudentEntity>
)