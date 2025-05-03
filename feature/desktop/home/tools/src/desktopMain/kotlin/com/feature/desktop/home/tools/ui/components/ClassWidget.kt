package com.feature.desktop.home.tools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.override.data.utils.data.ClassData

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Card onClick
@Composable
internal fun ClassWidget(
    classData: ClassData,
    isSelected: Boolean, // Nuevo parámetro para saber si está seleccionada
    onClick: () -> Unit, // Nuevo parámetro para manejar el click
    delete: () -> Unit,
    modifier: Modifier = Modifier // Permitir pasar modificadores externos
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .size(width = 220.dp, height = 180.dp), // Tamaño ligeramente ajustado
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.medium, // Usar formas de M3
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground // Un color de fondo sutil
        ),
        // Añadir un borde si está seleccionada, usando el color de la clase
        border = if (isSelected) BorderStroke(3.dp, classData.color) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Añadir un borde superior sutil con el color de la clase como acento
                // .border(BorderStroke(4.dp, classData.color), shape = RectangleShape) // Alternativa: borde superior
                .padding(16.dp), // Padding interno
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre elementos
        ) {
            // Nombre de la clase (más prominente)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = classData.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = classData.color // Usar el color como acento aquí
                )
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { delete() },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Deleted",
                            tint = colorScheme.error,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )
            }

            // Separador sutil
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // Otros detalles con tipografía más pequeña y color estándar
            Text(
                text = "${classData.career} - ${classData.degree}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface // Color estándar para legibilidad
            )
            Text(
                text = "Sección: ${classData.section}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Color ligeramente diferente
            )
        }
    }
}