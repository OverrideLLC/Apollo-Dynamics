package com.override.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "class_student_cross_ref",
    primaryKeys = ["class_id", "student_id"], // Clave primaria compuesta
    foreignKeys = [
        ForeignKey(
            entity = ClassEntity::class,
            parentColumns = ["class_id"],
            childColumns = ["class_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["student_id"],
            childColumns = ["student_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["class_id"]),
        Index(value = ["student_id"])
    ]
)
data class ClassStudentCrossRef(
    @ColumnInfo(name = "class_id")
    val classId: String,
    @ColumnInfo(name = "student_id")
    val studentId: String
)