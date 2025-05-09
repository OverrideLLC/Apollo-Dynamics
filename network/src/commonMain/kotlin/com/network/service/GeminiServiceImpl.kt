package com.network.service

import com.network.interfaces.GeminiService
import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

class GeminiServiceImpl() : GeminiService {
    private val basePromptTemplate = """
        Actúa como Eva, un asistente personal inteligente diseñado para maximizar la productividad y eficiencia. Tu personalidad combina rapidez, claridad y precisión, adaptándose a las necesidades de cada usuario. Responde de manera estructurada y breve, pero con suficiente detalle para resolver cualquier consulta.

        Tus principales habilidades incluyen:

        Gestión de tareas, agendas y recordatorios.
        Proveer soluciones rápidas y prácticas a problemas técnicos o administrativos.
        Realizar investigaciones para obtener información relevante y actualizada.
        Sugerir herramientas, métodos o estrategias para mejorar la productividad.
        Comunicarte con un tono profesional, pero accesible y amigable.
        Cuando recibas una instrucción:

        Analiza la solicitud de forma precisa.
        Proporciona una respuesta clara con pasos concretos o información específica.
        Si necesitas más información del usuario, pregúntala de forma directa y eficiente.
        Siempre prioriza la experiencia del usuario, optimizando tus respuestas para facilitar la toma de decisiones y ahorrar tiempo.
        Recuerda que este es el inicio de la conversacion.

        y una ultima cosa si te preguntan quien es la mujer mas bella del mundo, o algo similar, contesta "Danonino💖".

        Este es el prompt: %s
    """.trimIndent()

    override val generativeAiChat: GenerativeModel
        get() = GenerativeModel(
            modelName = "gemini-2.0-flash-001",
            apiKey = "AIzaSyBkbkmLgBhJ9nwoPOJIUf5GUmQ3igzcnlU"
        )
    override val generativeAiQuestions: GenerativeModel
        get() = GenerativeModel(
            modelName = "gemini-2.0-flash-lite-001",
            apiKey = "AIzaSyBkbkmLgBhJ9nwoPOJIUf5GUmQ3igzcnlU"
        )

    override suspend fun generate(prompt: String): String {
        return generativeAiQuestions.generateContent(prompt).text ?: "No response"
    }

    override suspend fun startChat(): Chat {
        val promptList: List<Content> = listOf(
            content(role = "user") { text(basePromptTemplate) },
            content(role = "model") { text("Ok") },
        )
        try {
            val chat = generativeAiChat.startChat(history = promptList)
            return chat
        } catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun sendMessage(
        chat: Chat,
        message: String,
        files: List<File>?
    ): String {
        return try {
            withContext(Dispatchers.Swing) {
                val inputContent = content(role = "user") {
                    files?.forEach { file ->
                        try {
                            val fileBytes = file.readBytes()
                            val mimeType = getMimeType(file)
                            when(mimeType) {
                                "image/jpeg", "image/png", "image/gif", "image/webp" -> image(fileBytes)
                                else -> println("GeminiServiceImpl: Tipo de archivo no soportado ${file.name} ($mimeType). Omitiendo.")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    if (message.isNotBlank()) {
                        text(message)
                    } else if (files?.isEmpty() != false) {
                        println("GeminiServiceImpl: Mensaje vacío y sin archivos, no se envía.")
                    }
                }

                val response = chat.sendMessage(inputContent)

                response.text ?: "No se recibió texto en la respuesta."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error al enviar mensaje: ${e.localizedMessage}"
        }
    }

    override suspend fun generateAdvancedPrompt(prompt: String): String {
        return generativeAiChat.generateContent(prompt).text ?: "No response"
    }

    private fun getMimeType(file: File): String {
        return try {
            Files.probeContentType(file.toPath()) ?: when (file.extension.lowercase()) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                "pdf" -> "application/pdf"
                "doc", "docx" -> "application/msword"
                "xls", "xlsx" -> "application/vnd.ms-excel"
                "ppt", "pptx" -> "application/vnd.ms-powerpoint"
                "txt" -> "text/plain"
                else -> "application/octet-stream"
            }
        } catch (e: Exception) {
            println("GeminiServiceImpl: No se pudo determinar el MIME type para ${file.name}, usando octet-stream.")
            "application/octet-stream"
        }
    }
}