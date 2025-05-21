// AnnouncementDao.kt
package com.override.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.override.data.entity.AnnouncementEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime // O LocalDate

@Dao
interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long // Devuelve el ID generado

    @Update
    suspend fun updateAnnouncement(announcement: AnnouncementEntity)

    @Query("SELECT * FROM announcements WHERE announcementId = :id LIMIT 1")
    suspend fun getAnnouncementById(id: Long): AnnouncementEntity?

    // Obtener todos los anuncios para una clase específica, ordenados por fecha descendente
    @Query("SELECT * FROM announcements WHERE ann_class_id = :classId ORDER BY ann_date DESC")
    suspend fun getAnnouncementsForClass(classId: String): List<AnnouncementEntity>

    // Obtener todos los anuncios para una clase específica como un Flow
    @Query("SELECT * FROM announcements WHERE ann_class_id = :classId ORDER BY ann_date DESC")
    fun getAnnouncementsForClassFlow(classId: String): Flow<List<AnnouncementEntity>>

    @Query("DELETE FROM announcements WHERE announcementId = :id")
    suspend fun deleteAnnouncementById(id: Long)

    @Query("DELETE FROM announcements WHERE ann_class_id = :classId")
    suspend fun deleteAnnouncementsForClass(classId: String)
}