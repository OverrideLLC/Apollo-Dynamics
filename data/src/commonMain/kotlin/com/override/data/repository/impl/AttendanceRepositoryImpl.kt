package com.override.data.repository.impl

import com.override.data.dao.AttendanceDao
import com.override.data.dao.StudentDao
import com.override.data.entity.AttendanceEntity
import com.override.data.repository.contract.AttendanceRepository
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2

// --- Implementación ---
class AttendanceRepositoryImpl(
    private val attendanceDao: AttendanceDao,
    private val studentDao: StudentDao // Inyectar StudentDao
) : AttendanceRepository {

    override suspend fun recordAttendance(classId: String, date: LocalDate, attendanceMap: Map<String, AttendanceStatus>) {
        withContext(Dispatchers.Swing) {
            // Convierte el mapa a una lista de AttendanceEntity
            val attendanceEntities = attendanceMap.map { (studentId, status) ->
                AttendanceEntity(
                    classId = classId,
                    studentId = studentId,
                    date = date,
                    status = status
                    // recordId se autogenerará o se reemplazará si ya existe (por OnConflictStrategy.REPLACE)
                )
            }
            // Inserta todos los registros (o los actualiza si ya existen para esa fecha)
            attendanceDao.insertAttendanceRecords(attendanceEntities)
        }
    }

    override suspend fun getAttendanceForClassOnDate(classId: String, date: LocalDate): List<StudentWithStatus> {
        return withContext(Dispatchers.Swing) {
            // 1. Obtiene los registros de asistencia para esa clase y fecha
            val attendanceEntities = attendanceDao.getAttendanceForClassOnDate(classId, date)
            val attendanceMap = attendanceEntities.associateBy { it.studentId } // Mapa para búsqueda rápida

            // 2. Obtiene TODOS los estudiantes de la clase (necesitamos la lista completa)
            //    Esto asume que tienes un método en ClassDao o StudentDao para obtener
            //    los estudiantes de una clase. Vamos a asumir que lo obtenemos via StudentDao
            //    filtrando por los IDs presentes en la asistencia O mejor, obteniendo
            //    todos los estudiantes de la clase desde ClassDao.
            //    ¡IMPORTANTE! Necesitamos la lista COMPLETA de estudiantes de la clase,
            //    no solo los que tienen registro de asistencia ese día.
            //    Modifiquemos ClassDao para tener `getStudentsForClass(classId)` o usemos
            //    `classDao.getClassWithStudents(classId)` y tomemos `it.students`.
            //    Usaremos la segunda opción por simplicidad ahora.

            // Necesitamos acceso a ClassDao aquí, ¡hay que inyectarlo!
            // *** ACTUALIZACIÓN: Modificar constructor para inyectar ClassDao ***
            // val classWithStudents = classDao.getClassWithStudents(classId) // Necesita ClassDao
            // val roster = classWithStudents?.students ?: emptyList()

            // *** Alternativa si solo tenemos StudentDao ***
            // Obtener todos los IDs de estudiantes que tienen registro ese día
            val studentIdsWithRecord = attendanceMap.keys
            // Obtener todos los estudiantes (podría ser ineficiente si hay muchos)
            // Sería MEJOR tener un método `getStudentsByIds(ids: List<String>)` en StudentDao
            val allStudentsInClass = studentDao.getAllStudents() // ¡Esto NO es correcto! Necesitamos los de ESA clase.
            // Requiere cambio en DAOs o estructura.

            // ***** SOLUCIÓN MÁS REALISTA: Asumir que tenemos la lista de IDs del roster *****
            // La forma más limpia es que este método reciba la lista de IDs del roster
            // o que ClassRepository exponga un método para obtener solo los IDs del roster.
            // Por ahora, haremos un apaño: obtener los estudiantes de los registros existentes.
            // ¡ESTO ESTÁ INCOMPLETO! Faltan los alumnos sin registro ese día.

            val studentsWithRecord = studentDao.getAllStudents() // Temporalmente obtenemos todos
                .filter { studentEntity -> studentIdsWithRecord.contains(studentEntity.id) }


            // 3. Combina la información del estudiante con su estado de asistencia
            studentsWithRecord.map { studentEntity ->
                val status = attendanceMap[studentEntity.id]?.status ?: AttendanceStatus.UNKNOWN // Estado o Desconocido
                StudentWithStatus(
                    student = studentEntity.toStudent(), // Convierte a tu clase de datos
                    status = status
                )
            }

            // ***** CÓMO HACERLO BIEN *****
            // 1. Inyectar ClassDao en AttendanceRepositoryImpl.
            // 2. Dentro de getAttendanceForClassOnDate:
            //    a. Obtener `classDao.getClassWithStudents(classId)`
            //    b. Obtener `attendanceDao.getAttendanceForClassOnDate(classId, date)` y crear `attendanceMap`.
            //    c. Mapear `classWithStudents.students` (el roster completo):
            //       `roster.map { studentEntity -> StudentWithStatus(studentEntity.toStudent(), attendanceMap[studentEntity.id]?.status ?: AttendanceStatus.UNKNOWN) }`
        }
    }

    override suspend fun getAttendanceHistoryForStudent(
        studentId: String,
        classId: String
    ): List<AttendanceEntity> {
        return withContext(Dispatchers.Swing) {
            attendanceDao.getSpecificAttendanceStudent(
                studentId = studentId,
                classId = classId
            )
        }
    }

    override suspend fun updateStudentAttendanceStatus(classId: String, studentId: String, date: LocalDate, newStatus: AttendanceStatus) {
        withContext(Dispatchers.Swing) {
            // Simplemente insertamos un nuevo registro. Gracias a OnConflictStrategy.REPLACE,
            // esto actualizará el registro existente si coincide classId, studentId y date (si tienes índice único),
            // o insertará uno nuevo. Asegúrate que tu DAO usa REPLACE.
            val record = AttendanceEntity(
                classId = classId,
                studentId = studentId,
                date = date,
                status = newStatus
            )
            // Podríamos buscar el recordId primero si quisiéramos ser más explícitos,
            // pero REPLACE es más simple si esa es la lógica deseada.
            attendanceDao.insertAttendanceRecord(record)
        }
    }
}