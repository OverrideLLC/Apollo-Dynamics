package com.override.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.override.data.dao.AttendanceDao
import com.override.data.dao.ClassDao
import com.override.data.dao.StudentDao
import com.override.data.entity.AttendanceEntity
import com.override.data.entity.ClassEntity
import com.override.data.entity.ClassStudentCrossRef
import com.override.data.entity.StudentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing

@Database(
    entities = [
        StudentEntity::class,
        ClassEntity::class,
        AttendanceEntity::class,
        ClassStudentCrossRef::class // No olvides la tabla de unión
    ],
    version = 1, // Incrementa si cambias el esquema más adelante
    exportSchema = false // Mantenlo en false por ahora para simplicidad
)
@TypeConverters(Converters::class) // Registra tus conversores aquí
abstract class AppDatabase : RoomDatabase() {

    // Métodos abstractos para obtener cada DAO
    abstract fun studentDao(): StudentDao
    abstract fun classDao(): ClassDao
    abstract fun attendanceDao(): AttendanceDao
}

fun getDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Swing)
        .build()
}