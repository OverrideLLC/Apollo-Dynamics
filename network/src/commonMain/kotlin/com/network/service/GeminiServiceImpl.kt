package com.network.service

import com.network.interfaces.GeminiService
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel

class GeminiServiceImpl() : GeminiService {
    override val generativeAi: GenerativeModel
        get() = GenerativeModel(
            modelName = "gemini-2.0-flash-thinking-exp-01-21",
            apiKey = "AIzaSyBkbkmLgBhJ9nwoPOJIUf5GUmQ3igzcnlU"
        )

    override suspend fun generate(prompt: String): String {
        return try {
            generativeAi.generateContent(prompt).text ?: "No response"
        } catch (e: Exception) {
            return e.message.toString()
        }
    }
}