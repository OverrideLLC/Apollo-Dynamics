package com.shared.utils.data.firebase

data class Student(
    val id: String,
    val name: String? = null,
    val email: String? = null,
    val courses: List<Course>? = null,
)