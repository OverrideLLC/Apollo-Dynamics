package com.network.credentials // Asegúrate de que este sea tu paquete correcto

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
// Importa las APIs de Compose Resources
import com.shared.resources.Res // Asumiendo que esta es tu clase generada por KMP Resources
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes // Asegúrate que esta sea la importación correcta si no usas Res
// Importa Dispatchers y withContext para cambiar de hilo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

/**
 * Objeto Singleton para manejar la obtención de credenciales OAuth 2.0 de Google
 * para la API de Classroom usando el flujo de aplicación instalada (escritorio).
 * Utiliza Compose Resources para cargar el archivo de credenciales y maneja
 * correctamente las operaciones bloqueantes.
 */
object ClassroomCredentials {

    // Fábrica para el manejo de JSON
    private val JSON_FACTORY = GsonFactory.getDefaultInstance()
    // Directorio para almacenar tokens persistentes
    private const val TOKENS_DIRECTORY_PATH = "tokens"
    // Ruta al archivo de credenciales dentro de composeResources
    private const val CREDENTIALS_RESOURCE_PATH = "files/classroom.json" // Asegúrate que el archivo se llame así

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
    @OptIn(
        ExperimentalResourceApi::class,
        InternalResourceApi::class // Puede que no necesites InternalResourceApi dependiendo de tu setup
    )
    @Throws(IOException::class, Exception::class)
    suspend fun getCredentials(
        httpTransport: NetHttpTransport,
        scopes: Collection<String>
    ): Credential {

        // --- 1. Carga de secretos del cliente (puede suspender) ---
        val credentialBytes = try {
            // Lee los bytes del recurso. Asume que Res.getUri y Res.readBytes funcionan como esperas.
            // Si readResourceBytes está disponible y funciona, podría ser más directo:
            // readResourceBytes(CREDENTIALS_RESOURCE_PATH)
            Res.readBytes(CREDENTIALS_RESOURCE_PATH)
        } catch (e: Exception) {
            // Relanza como IOException para consistencia o maneja específicamente
            throw IOException("Failed to read credentials resource: $CREDENTIALS_RESOURCE_PATH", e)
        }

        // Convierte bytes a InputStream y luego a GoogleClientSecrets
        val clientSecrets = credentialBytes.inputStream().use { stream ->
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(stream))
        }

        // --- 2. Configuración del Flujo OAuth (síncrono) ---
        val dataStoreFactory = FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH))
        val flow = GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, scopes)
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
            } catch(ioe: IOException) {
                println("Authorization failed: ${ioe.message}") // Log de error
                throw ioe // Relanza la excepción para que sea manejada por quien llama
            } finally {
                println("Authorization attempt finished.") // Log para depuración
            }
        }
    }
}
