package com.shared.utils.data.firebase

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String? = null,
    val name: String? = null,
    val degree: String? = null,
    val section: String? = null,
    val career: String? = null,
    val studentsId: List<String>? = null,
    val attendance: Map</*date dd/mm*/Timestamp, List<Attendance>>? = null,
)
