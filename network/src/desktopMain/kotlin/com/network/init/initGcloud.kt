package com.network.init

import com.network.utils.constants.Constants
import com.shared.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
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
@OptIn(ExperimentalResourceApi::class)
suspend fun initGcloudFromPath() {
    try {
        Res.readBytes(Constants.CREDENTIALS_RESOURCE_PATH_GOOGLE_CLOUD)
    } catch (e: Exception) {
        throw IOException("Failed to read credentials resource: ${Constants.CREDENTIALS_RESOURCE_PATH_GOOGLE_CLOUD}", e)
    }
}