package com.override.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.override.data.entity.AttendanceEntity
import kotlinx.datetime.LocalDate

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Reemplaza si ya existe registro para ese alumno/clase/fecha
    suspend fun insertAttendanceRecord(record: AttendanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecords(records: List<AttendanceEntity>)

    // Obtener todos los registros para una clase específica en una fecha específica
    @Query("""
        SELECT * FROM attendance_records
        WHERE record_class_id = :classId AND record_date = :date
    """)
    suspend fun getAttendanceForClassOnDate(classId: String, date: LocalDate): List<AttendanceEntity>

    // Obtener todos los registros de asistencia para una clase (historial)
    @Query("SELECT * FROM attendance_records WHERE record_class_id = :classId ORDER BY record_date DESC")
    suspend fun getAttendanceHistoryForClass(classId: String): List<AttendanceEntity>

    // Obtener un registro específico (útil para actualizaciones)
     @Query("""
        SELECT * FROM attendance_records
        WHERE record_class_id = :classId AND record_student_id = :studentId AND record_date = :date
        LIMIT 1
    """)
    suspend fun getSpecificAttendanceRecord(classId: String, studentId: String, date: LocalDate): AttendanceEntity?

     // Borrar todos los registros para una clase en una fecha específica
     @Query("DELETE FROM attendance_records WHERE record_class_id = :classId AND record_date = :date")
     suspend fun deleteAttendanceForClassOnDate(classId: String, date: LocalDate)

     // Borrar un registro específico
     @Query("DELETE FROM attendance_records WHERE recordId = :recordId")
     suspend fun deleteAttendanceById(recordId: Long)
}