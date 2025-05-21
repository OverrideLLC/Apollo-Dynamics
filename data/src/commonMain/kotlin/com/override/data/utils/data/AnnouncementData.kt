// AnnouncementData.kt (colócalo en tu paquete de utils.data o similar)
package com.override.data.utils.data

import com.override.data.entity.AnnouncementEntity
import kotlinx.datetime.LocalDateTime // O LocalDate

data class AnnouncementData(
    val id: Long = 0,
    val classId: String,
    val title: String,
    val message: String,
    val date: LocalDateTime // O LocalDate
)

// Funciones de mapeo (puedes ponerlas dentro de las clases o como extensiones)

fun AnnouncementEntity.toAnnouncementData(): AnnouncementData {
    return AnnouncementData(
        id = this.announcementId,
        classId = this.classId,
        title = this.title,
        message = this.message,
        date = this.date?: LocalDateTime.parse("1970-01-01T00:00:00")
    )
}

fun AnnouncementData.toEntity(): AnnouncementEntity {
    return AnnouncementEntity(
        announcementId = this.id,
        classId = this.classId,
        title = this.title,
        message = this.message,
        date = this.date
    )
}