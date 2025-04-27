package com.feature.desktop.home.ai.utils

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import java.awt.Desktop
import java.net.URI
import java.net.URISyntaxException

// Función auxiliar para abrir URLs en Desktop de forma segura
fun openUrlInBrowser(url: String) {
    try {
        val desktop = Desktop.getDesktop()
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(url))
        } else {
            println("Desktop browse action not supported.")
            // Considerar un fallback o log
        }
    } catch (e: Exception) {
        // Captura URISyntaxException, IOException, SecurityException, etc.
        println("Error opening URL '$url': ${e.message}")
        e.printStackTrace()
        // Mostrar feedback al usuario si es necesario
    }
}

// Función auxiliar para extraer un nombre de host simple de una URL
fun extractHostName(url: String): String {
    return try {
        val uri = URI(url)
        // Obtener el host (ej: www.google.com)
        // Devolver el host o la URL original si falla
        uri.host ?: url.removePrefix("https://").removePrefix("http://").substringBefore('/')
    } catch (e: URISyntaxException) {
        // Si la URL no es válida, devolverla tal cual o una versión simplificada
        url.removePrefix("https://").removePrefix("http://").substringBefore('/')
    }
}