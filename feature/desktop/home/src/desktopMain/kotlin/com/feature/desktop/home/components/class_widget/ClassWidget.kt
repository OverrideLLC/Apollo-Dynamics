package com.feature.desktop.home.components.class_widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Usar Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.utils.data.ClassData

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Card onClick
@Composable
internal fun ClassWidget(
    classData: ClassData,
    isSelected: Boolean, // Nuevo parámetro para saber si está seleccionada
    onClick: () -> Unit, // Nuevo parámetro para manejar el click
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Un color de fondo sutil
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
            Text(
                text = classData.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = classData.color // Usar el color como acento aquí
            )

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