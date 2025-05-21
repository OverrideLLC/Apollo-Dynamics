// AnnouncementEntity.kt
package com.override.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.override.data.db.Converters
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime // O LocalDate si solo necesitas la fecha

@Entity(
    tableName = "announcements",
    foreignKeys = [
        ForeignKey(
            entity = ClassEntity::class,
            parentColumns = ["class_id"],
            childColumns = ["ann_class_id"],
            onDelete = ForeignKey.CASCADE // Si se borra la clase, se borran sus anuncios
        )
    ],
    indices = [
        Index(value = ["ann_class_id"]),
        Index(value = ["ann_date"])
    ]
)
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true)
    val announcementId: Long = 0,

    @ColumnInfo(name = "ann_class_id")
    val classId: String, // FK a ClassEntity

    @ColumnInfo(name = "ann_title")
    val title: String,

    @ColumnInfo(name = "ann_message")
    val message: String,

    @ColumnInfo(name = "ann_date")
    val date: LocalDateTime?
)