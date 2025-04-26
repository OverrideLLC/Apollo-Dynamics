package com.feature.desktop.home.utils.data

import com.feature.desktop.home.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate

data class AttendanceRecord(
    val date: LocalDate,
    // Mapa de Student ID a su estado de asistencia para ESE día
    val attendance: Map<String, AttendanceStatus>
)
