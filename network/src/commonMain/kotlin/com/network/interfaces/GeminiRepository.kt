package com.network.interfaces

interface GeminiRepository {
    suspend fun generate(prompt: String): String
}