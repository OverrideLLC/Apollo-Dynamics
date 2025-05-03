package com.feature.desktop.home.tools.ui.components.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shared.ui.VerticalDivider

/**
 * Composable que representa el encabezado de la hoja de asistencia.
 * Este componente muestra las etiquetas "No.", "Student Name" y "Status" en una fila.
 *
 * @param modifier Modificador para personalizar el diseño del encabezado.
 */
@Composable
internal fun AttendanceSheetHeader(modifier: Modifier = Modifier) {
    Row(
        /**
         * Configura la fila para que ocupe todo el ancho disponible,
         * establezca un color de fondo, un borde, un relleno vertical y
         * ajuste la altura al contenido.
         */
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min),
        /**
         * Centra verticalmente el contenido dentro de la fila.
         */
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            "No.",
            Modifier.weight(0.5f).padding(horizontal = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        VerticalDivider()
        Text(
            "Student Name",
            Modifier.weight(3f).padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        VerticalDivider()
        Text(
            "Status",
            Modifier.weight(4f).padding(horizontal = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
