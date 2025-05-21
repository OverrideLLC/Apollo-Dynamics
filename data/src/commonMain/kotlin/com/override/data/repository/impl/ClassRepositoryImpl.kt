package com.override.data.repository.impl

import com.override.data.dao.AnnouncementDao
import com.override.data.dao.AttendanceDao
import com.override.data.dao.ClassDao
import com.override.data.utils.data.ClassWithStudents
import com.override.data.entity.AttendanceEntity
import com.override.data.entity.ClassEntity
import com.override.data.entity.ClassStudentCrossRef
import com.override.data.repository.contract.ClassRepository
import com.override.data.utils.data.AttendanceRecord
import com.override.data.utils.data.ClassData
import com.override.data.utils.data.Student
import com.override.data.utils.data.toAnnouncementData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class ClassRepositoryImpl(
    private val classDao: ClassDao,
    private val attendanceDao: AttendanceDao,
    private val announcementDao: AnnouncementDao
) : ClassRepository {

    override fun getAllClasses(): Flow<List<ClassData>> {
        // Obtiene el flujo de ClassWithStudents
        return classDao.getAllClassesWithStudentsFlow().map { classesWithStudents ->
            // Para cada ClassWithStudents, necesitamos obtener su historial de asistencia
            // y luego mapear todo a ClassData. Esto puede ser costoso si hay muchas clases.
            // Una alternativa es cargar el historial solo cuando se selecciona una clase.
            // Aquí lo cargamos todo para el ejemplo.
            withContext(Dispatchers.Swing) { // Asegura que las operaciones de BD se hagan en IO
                classesWithStudents.map { classWithStudents ->
                    mapToClassData(classWithStudents) // Llama a función helper
                }
            }
        }
    }

    override suspend fun getClassById(id: String): ClassData? {
        // Es MUY RECOMENDABLE usar Dispatchers.IO para operaciones de base de datos
        // Dispatchers.Swing es para la interfaz de usuario en Swing y puede bloquearla.
        return withContext(Dispatchers.Swing) {
            // 1. Obtener la clase con sus estudiantes
            val classWithStudents = classDao.getClassWithStudents(id) //

            // 2. Obtener los anuncios para esa clase
            // Asumo que tienes una función en AnnouncementDao como la que creamos antes:
            // @Query("SELECT * FROM announcements WHERE ann_class_id = :classId ORDER BY ann_date DESC")
            // suspend fun getAnnouncementsForClass(classId: String): List<AnnouncementEntity>
            val announcementEntities = announcementDao.getAnnouncementsForClass(id)
            val announcementsData = announcementEntities.map { it.toAnnouncementData() }

            // 3. Mapear a ClassData
            //    classWithStudents?.let { cws -> mapToClassData(cws, announcementsData) }
            //    O si tu mapToClassData no puede tomar anuncios directamente,
            //    hazlo a través de ClassEntity.toClassData

            classWithStudents?.let { cws ->
                // cws.classEntity es tu ClassEntity
                // cws.students es tu List<StudentEntity>
                cws.classEntity.toClassData(
                    students = cws.students,
                    announcements = announcementsData
                    // Aquí también podrías cargar y pasar el historial de asistencia si fuera necesario
                )
            }
        }
    }

    override suspend fun addOrUpdateClass(classData: ClassData) {
        withContext(Dispatchers.Swing) {
            // 1. Inserta o actualiza la entidad de la clase
            val classEntity = ClassEntity.fromClassData(classData)
            classDao.insertClass(classEntity)

            // 2. Crea las referencias cruzadas para el roster
            // Asegúrate que el tipo Student aquí es el correcto (com.feature.desktop.home.utils.data.Student)
            val crossRefs = classData.roster.map { student: Student ->
                ClassStudentCrossRef(classId = classData.id, studentId = student.id)
            }
            // Considera una estrategia más robusta para actualizar las referencias cruzadas
            // (borrar las antiguas y añadir las nuevas, o calcular el delta).
            // Por ahora, usamos insert con OnConflictStrategy.IGNORE (asumido en el DAO).
            classDao.insertClassStudentCrossRefs(crossRefs)

            // 3. Inserta o actualiza los registros de asistencia (si los hay)
            // *** ALTERNATIVA SIN flatMap USANDO BUCLES ANIDADOS ***
            val attendanceEntities =
                mutableListOf<AttendanceEntity>() // Crea una lista mutable vacía
            classData.attendanceHistory.forEach { record -> // Itera sobre cada AttendanceRecord
                for (entry in record.attendance.entries) {
                    val studentId = entry.key
                    val status = entry.value
                    // Crea una AttendanceEntity para cada entrada del mapa
                    val entity = AttendanceEntity(
                        classId = classData.id,
                        studentId = studentId,
                        date = record.date,
                        status = status
                    )
                    attendanceEntities.add(entity) // Añade la entidad a la lista
                }
            }

            // Ahora attendanceEntities contiene la lista aplanada de todas las entidades de asistencia
            if (attendanceEntities.isNotEmpty()) {
                // Asume OnConflictStrategy.REPLACE en el DAO para manejar actualizaciones
                attendanceDao.insertAttendanceRecords(attendanceEntities)
            }
        }
    }

    override suspend fun deleteClass(classId: String) {
        withContext(Dispatchers.Swing) {
            classDao.deleteClassById(classId)
            // Las relaciones (ClassStudentCrossRef, AttendanceEntity)
            // se borrarán automáticamente si onDelete = ForeignKey.CASCADE está configurado.
        }
    }

    override suspend fun addStudentToClass(classId: String, studentId: String) {
        withContext(Dispatchers.Swing) {
            val crossRef = ClassStudentCrossRef(classId = classId, studentId = studentId)
            classDao.insertClassStudentCrossRef(crossRef)
        }
    }

    override suspend fun removeStudentFromClass(classId: String, studentId: String) {
        withContext(Dispatchers.Swing) {
            classDao.deleteStudentFromClass(classId, studentId)
            // Considera si también quieres borrar los registros de asistencia de ese alumno
            // para esa clase, o mantenerlos como histórico.
            // attendanceDao.deleteAttendanceForStudentInClass(classId, studentId) // Método hipotético
        }
    }

    override suspend fun getAttendanceDatesForClass(classId: String): List<LocalDate> {
        return withContext(Dispatchers.Swing) {
            // Obtenemos todos los registros y extraemos las fechas únicas
            val history = attendanceDao.getAttendanceHistoryForClass(classId)
            history.map { it.date }.distinct().sortedDescending()
        }
    }


    // --- Función Helper para Mapear ---
    private suspend fun mapToClassData(classWithStudents: ClassWithStudents): ClassData {
        // Ya tenemos la clase y los estudiantes. Ahora obtenemos el historial de asistencia.
        val attendanceHistoryEntities =
            attendanceDao.getAttendanceHistoryForClass(classWithStudents.classEntity.id)

        // Agrupamos los registros de asistencia por fecha
        val attendanceHistoryRecords = attendanceHistoryEntities
            .groupBy { it.date } // Agrupa por LocalDate
            .map { (date, entitiesOnDate) ->
                // Para cada fecha, crea el mapa de Student ID -> Status
                val attendanceMap = entitiesOnDate.associate { it.studentId to it.status }
                AttendanceRecord(date = date, attendance = attendanceMap)
            }

        // Mapeamos las entidades de estudiante a la clase de datos Student
        val roster = classWithStudents.students.map { it.toStudent() }

        // Creamos la instancia final de ClassData
        return ClassData(
            id = classWithStudents.classEntity.id,
            name = classWithStudents.classEntity.name,
            roster = roster,
            color = classWithStudents.classEntity.color,
            degree = classWithStudents.classEntity.degree,
            career = classWithStudents.classEntity.career,
            section = classWithStudents.classEntity.section,
            attendanceHistory = attendanceHistoryRecords.reversed()
        )
    }
}
