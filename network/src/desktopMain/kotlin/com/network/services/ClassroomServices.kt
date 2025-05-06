package com.network.services

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.classroom.Classroom
import com.network.utils.constants.Constants
import com.shared.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class ClassroomServices(
    private val jsonFactory: GsonFactory,
    private val httpTransport: NetHttpTransport
) {
    /**
     * Obtiene un objeto Credential autorizado. Es una función suspendida porque
     * lee recursos de forma asíncrona y realiza operaciones de red/bloqueantes
     * en un contexto apropiado (Dispatchers.IO).
     *
     * @param httpTransport La instancia de transporte HTTP.
     * @param scopes La colección de scopes requeridos.
     * @return Un objeto Credential autorizado.
     * @throws IOException Si hay problemas de red, de lectura de archivo o durante la autorización.
     * @throws Exception Si el recurso no se encuentra u ocurre otro error.
     */
    @OptIn(ExperimentalResourceApi::class)
    @Throws(IOException::class, Exception::class)
    private suspend fun getCredentials(
        scopes: Collection<String>
    ): Credential {

        // --- 1. Carga de secretos del cliente (puede suspender) ---
        val credentialBytes = try {
            // Lee los bytes del recurso. Asume que Res.getUri y Res.readBytes funcionan como esperas.
            // Si readResourceBytes está disponible y funciona, podría ser más directo:
            // readResourceBytes(CREDENTIALS_RESOURCE_PATH)
            Res.readBytes(Constants.CREDENTIALS_RESOURCE_PATH)
        } catch (e: Exception) {
            // Relanza como IOException para consistencia o maneja específicamente
            throw IOException(
                "Failed to read credentials resource: ${Constants.CREDENTIALS_RESOURCE_PATH}",
                e
            )
        }

        // Convierte bytes a InputStream y luego a GoogleClientSecrets
        val clientSecrets = credentialBytes.inputStream().use { stream ->
            GoogleClientSecrets.load(jsonFactory, InputStreamReader(stream))
        }

        // --- 2. Configuración del Flujo OAuth (síncrono) ---
        val dataStoreFactory = FileDataStoreFactory(File(Constants.TOKENS_DIRECTORY_PATH))
        val flow =
            GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        // --- 3. Autorización (Bloqueante - ¡Mover a Dispatchers.IO!) ---
        // Usa withContext para cambiar al despachador IO ANTES de la llamada bloqueante.
        return withContext(Dispatchers.IO) {
            println("Attempting to authorize user (blocking operation on IO dispatcher)...") // Log para depuración
            try {
                AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
            } catch (ioe: IOException) {
                println("Authorization failed: ${ioe.message}") // Log de error
                throw ioe // Relanza la excepción para que sea manejada por quien llama
            } finally {
                println("Authorization attempt finished.") // Log para depuración
            }
        }
    }

    suspend fun initializeClassroomClient(
        scopes: Collection<String>,
    ): Classroom {
        return try {
            withContext(Dispatchers.Swing) {
                val transport = NetHttpTransport()
                val jsonFactory = GsonFactory.getDefaultInstance()
                val credential = getCredentials(scopes)

                Classroom.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Apollo")
                    .build()
            }

        } catch (e: IOException) {
            println("Error initializing Classroom service: ${e.message}")
            e.printStackTrace()
            throw e
        } catch (t: Throwable) {
            println("Unexpected error initializing Classroom service: ${t.message}")
            t.printStackTrace()
            throw t
        } finally {
            println("Classroom service initialization complete.")
        }
    }
}