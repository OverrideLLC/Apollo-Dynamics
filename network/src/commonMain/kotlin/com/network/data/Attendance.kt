package com.shared.utils.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class Attendance(
    val studentId: String? = null,
    val status: String? = null,
)