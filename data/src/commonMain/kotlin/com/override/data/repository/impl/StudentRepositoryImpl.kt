package com.override.data.repository.impl

import com.override.data.dao.StudentDao
import com.override.data.entity.StudentEntity
import com.override.data.repository.contract.StudentRepository
import com.override.data.utils.data.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

class StudentRepositoryImpl(
    private val studentDao: StudentDao // Inyecta el DAO
) : StudentRepository {

    override fun getAllStudents(): Flow<List<Student>> {
        // Mapea el flujo de StudentEntity a un flujo de Student
        return studentDao.getAllStudentsFlow().map { entityList ->
            entityList.map { it.toStudent() } // Usa la función de conversión
        }
    }

    override suspend fun getStudentById(id: String): Student? {
        // Ejecuta en el contexto de IO y mapea el resultado
        return withContext(Dispatchers.Swing) {
            studentDao.getStudentById(id)?.toStudent()
        }
    }

    override suspend fun addOrUpdateStudent(student: Student) {
        // Ejecuta en el contexto de IO y convierte Student a StudentEntity
        withContext(Dispatchers.Swing) {
            studentDao.insertStudent(StudentEntity.fromStudent(student))
        }
    }

     override suspend fun addOrUpdateStudents(students: List<Student>) {
        withContext(Dispatchers.Swing) {
            val entities = students.map { StudentEntity.fromStudent(it) }
            studentDao.insertStudents(entities)
        }
    }

    override suspend fun deleteStudent(studentId: String) {
        withContext(Dispatchers.Swing) {
            studentDao.deleteStudentById(studentId)
            // Nota: Las relaciones (asistencia, inscripción a clases) se borrarán
            // automáticamente si configuraste onDelete = ForeignKey.CASCADE
        }
    }
}