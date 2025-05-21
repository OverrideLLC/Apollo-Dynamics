// AnnouncementRepositoryImpl.kt (en tu paquete de implementación de repositorios)
package com.override.data.repository.impl

import com.override.data.dao.AnnouncementDao
import com.override.data.entity.AnnouncementEntity
import com.override.data.repository.contract.AnnouncementRepository
import com.override.data.utils.data.AnnouncementData
import com.override.data.utils.data.toAnnouncementData
import com.override.data.utils.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AnnouncementRepositoryImpl(
    private val announcementDao: AnnouncementDao
) : AnnouncementRepository {

    override suspend fun createAnnouncement(classId: String, title: String, message: String): Long {
        val newAnnouncement = AnnouncementEntity(
            classId = classId,
            title = title,
            message = message,
            date = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()) // O toLocalDate si usas LocalDate
        )
        return announcementDao.insertAnnouncement(newAnnouncement)
    }

    override suspend fun getAnnouncementById(announcementId: Long): AnnouncementData? {
        return announcementDao.getAnnouncementById(announcementId)?.toAnnouncementData()
    }

    override fun getAnnouncementsForClass(classId: String): Flow<List<AnnouncementData>> {
        return announcementDao.getAnnouncementsForClassFlow(classId).map { entities ->
            entities.map { it.toAnnouncementData() }
        }
    }

    override suspend fun updateAnnouncement(announcement: AnnouncementData) {
        announcementDao.updateAnnouncement(announcement.toEntity())
    }

    override suspend fun deleteAnnouncement(announcementId: Long) {
        announcementDao.deleteAnnouncementById(announcementId)
    }

    override suspend fun deleteAnnouncementsForClass(classId: String) {
        announcementDao.deleteAnnouncementsForClass(classId)
    }
}