package com.override.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo // Opcional para nombres de columna explícitos
import com.override.data.utils.data.Student

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey // Usamos el id existente como clave primaria
    @ColumnInfo(name = "student_id") // Renombrar columna es opcional
    val id: String,

    val name: String,
    val email: String,
    val number: String, // Asegúrate que estos 'number' y 'controlNumber' son necesarios
    val controlNumber: Int
) {
    // Puedes añadir un constructor secundario o función estática si necesitas
    // convertir fácilmente desde tu clase original `Student`
    companion object {
        fun fromStudent(student: Student): StudentEntity {
            return StudentEntity(
                id = student.id,
                name = student.name,
                email = student.email,
                number = student.number,
                controlNumber = student.controlNumber
            )
        }
    }
    // Y una función para convertir de vuelta si es necesario
    fun toStudent(): Student {
         return Student(
             id = this.id,
             name = this.name,
             email = this.email,
             number = this.number,
             controlNumber = this.controlNumber
         )
    }
}