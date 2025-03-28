package com.network.interfaces

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel

interface GeminiService {
    val generativeAi: GenerativeModel
    suspend fun generate(prompt: String): String
}