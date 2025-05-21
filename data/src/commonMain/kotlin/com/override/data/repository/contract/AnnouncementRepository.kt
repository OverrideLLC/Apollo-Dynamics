// AnnouncementRepository.kt
package com.override.data.repository.contract

import com.override.data.utils.data.AnnouncementData
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun createAnnouncement(classId: String, title: String, message: String): Long

    suspend fun getAnnouncementById(announcementId: Long): AnnouncementData?

    fun getAnnouncementsForClass(classId: String): Flow<List<AnnouncementData>>

    suspend fun updateAnnouncement(announcement: AnnouncementData)

    suspend fun deleteAnnouncement(announcementId: Long)

    suspend fun deleteAnnouncementsForClass(classId: String)
}