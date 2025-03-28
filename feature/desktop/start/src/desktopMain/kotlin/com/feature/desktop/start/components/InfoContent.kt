package com.feature.desktop.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.Desktop
import java.net.URI

@Composable
internal fun InfoContent() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Inicia sesión en Apollo Dynamics",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.tertiary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Sigue las instrucciones a continuación para iniciar sesión de forma segura:",
            style = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.tertiary
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = """
                        1. Abre la aplicación Quickness en tu teléfono.
                        2. Haz click en la camará.
                        3. Escanea el código QR que aparece en esta pantalla.
                    """.trimIndent(),
            style = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.tertiary,
                lineHeight = 24.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextButton(
            onClick = { Desktop.getDesktop().browse(URI.create("https://authoverride.web.app/")) },
            content = {
                Text(
                    text = "¿Necesitas ayuda para empezar?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.tertiary
                    ),
                )
            }
        )
    }
}
