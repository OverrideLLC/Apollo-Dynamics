package com.network.interfaces

import dev.shreyaspatil.ai.client.generativeai.Chat
import java.io.File

interface GeminiRepository {
    suspend fun generate(prompt: String): String
    suspend fun startChat(): Chat
    suspend fun sendMessage(chat: Chat, message: String, files: List<File>? = null): String
}