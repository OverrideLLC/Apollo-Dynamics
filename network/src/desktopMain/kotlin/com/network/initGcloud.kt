package com.network

import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Inicializa las credenciales de Google Cloud cargando una clave de cuenta de servicio
 * desde la ruta especificada en el sistema de archivos.
 *
 * @param credentialsPath La ruta absoluta o relativa al archivo JSON de la clave de cuenta de servicio.
 * @return Un objeto GoogleCredentials listo para usar.
 * @throws IllegalArgumentException Si la ruta es nula o vacía.
 * @throws IllegalStateException Si el archivo no se encuentra, no se puede leer o es inválido.
 */
fun initGcloudFromPath(credentialsPath: String?) {
    // 1. Validar que la ruta no sea nula o vacía
    if (credentialsPath.isNullOrBlank()) {
        throw IllegalArgumentException("La ruta al archivo de credenciales no puede ser nula o estar vacía.")
    }

    println("Intentando cargar credenciales desde la ruta: $credentialsPath") // Ayuda para depurar

    try {
        // 2. Crear un FileInputStream para leer el archivo desde la ruta
        //    Usamos 'use' para asegurar que el stream se cierre automáticamente,
        //    incluso si ocurren errores.
        FileInputStream(credentialsPath)
    } catch (e: FileNotFoundException) {
        // Error específico si el archivo no existe en esa ruta
        throw IllegalStateException("Error: Archivo de credenciales no encontrado en la ruta especificada: $credentialsPath", e)
    } catch (e: IOException) {
        // Otros errores de lectura o al parsear el JSON
        throw IllegalStateException("Error al leer o procesar el archivo de credenciales desde la ruta $credentialsPath: ${e.message}", e)
    } catch (e: Exception) {
        // Captura cualquier otro error inesperado
        throw IllegalStateException("Error inesperado al cargar credenciales desde la ruta $credentialsPath: ${e.message}", e)
    }
}