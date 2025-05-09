package com.network.interfaces

import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import java.io.File

interface GeminiService {
    val generativeAiChat: GenerativeModel
    val generativeAiQuestions: GenerativeModel
    suspend fun generate(prompt: String): String
    suspend fun startChat(): Chat
    suspend fun sendMessage(chat: Chat, message: String, files: List<File>?): String
    suspend fun generateAdvancedPrompt(prompt: String): String
}