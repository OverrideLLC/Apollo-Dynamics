package com.override.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.override.data.db.AppDatabase
import java.io.File

fun getDesktopDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val db = File(System.getProperty("java.io.tmpdir"),  "app_database")
    return Room
        .databaseBuilder<AppDatabase>(
            db.absolutePath,
        )
}