package com.override.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate // Necesita TypeConverter

@Entity(
    tableName = "attendance_records",
    // Claves foráneas para asegurar integridad referencial
    foreignKeys = [
        ForeignKey(
            entity = ClassEntity::class,
            parentColumns = ["class_id"],
            childColumns = ["record_class_id"],
            onDelete = ForeignKey.CASCADE // Si se borra la clase, se borran sus registros
        ),
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["student_id"],
            childColumns = ["record_student_id"],
            onDelete = ForeignKey.CASCADE // Si se borra el alumno, se borran sus registros
        )
    ],
    // Índices para mejorar rendimiento de consultas (opcional pero recomendado)
    indices = [
        Index(value = ["record_class_id"]),
        Index(value = ["record_student_id"]),
        Index(value = ["record_date"]),
        // Índice único para evitar duplicados (mismo alumno, misma clase, misma fecha)
        Index(value = ["record_class_id", "record_student_id", "record_date"], unique = true)
    ]
)
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0, // Nueva clave primaria para esta tabla

    @ColumnInfo(name = "record_class_id")
    val classId: String, // FK a ClassEntity

    @ColumnInfo(name = "record_student_id")
    val studentId: String, // FK a StudentEntity

    @ColumnInfo(name = "record_date")
    val date: LocalDate, // Necesita TypeConverter

    @ColumnInfo(name = "record_status")
    val status: AttendanceStatus // Necesita TypeConverter
)