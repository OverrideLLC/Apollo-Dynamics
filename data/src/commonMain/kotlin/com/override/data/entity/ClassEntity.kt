package com.override.data.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.override.data.utils.data.ClassData

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey
    @ColumnInfo(name = "class_id")
    val id: String,

    val name: String,
    val color: Color, // Necesita TypeConverter
    val degree: String,
    val career: String,
    val section: String,

    // Ya NO incluimos roster ni attendanceHistory aquí
) {
    // --- CAMPOS IGNORADOS (fuera del constructor primario) ---
    @Ignore
    var roster: List<StudentEntity> = emptyList() // Usa 'var' si necesitas modificarlos después

    @Ignore
    var attendanceHistory: List<AttendanceEntity> = emptyList() // Usa 'var'

    companion object {
        fun fromClassData(classData: ClassData): ClassEntity {
            return ClassEntity(
                id = classData.id,
                name = classData.name,
                color = classData.color,
                degree = classData.degree,
                career = classData.career,
                section = classData.section
                // roster y attendanceHistory se cargan por separado
            )
        }
    }

    // Función para convertir de vuelta (parcialmente, las listas se cargan por DAO)
    fun toClassData(
        students: List<StudentEntity> = emptyList(),
        attendance: List<AttendanceEntity> = emptyList()
    ): ClassData {
        // La conversión de attendance es más compleja, requiere agrupar por fecha
        // Esto es solo un ejemplo básico
        return ClassData(
            id = this.id,
            name = this.name,
            color = this.color,
            degree = this.degree,
            career = this.career,
            section = this.section,
            roster = students.map { it.toStudent() }
            // attendanceHistory requeriría procesamiento adicional
        )
    }
}