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
import androidx.compose.material3.HorizontalDivider
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

/**
 * Componente composable que representa un widget de clase.
 * Muestra la información de una clase en un formato de tarjeta.
 *
 * @param classData Datos de la clase que se van a mostrar.
 * @param isSelected Booleano que indica si la clase está seleccionada.
 * @param onClick Función lambda que se ejecuta cuando se hace clic en la tarjeta.
 * @param delete Función lambda que se ejecuta cuando se hace clic en el icono de eliminar.
 * @param modifier Modificador para personalizar el aspecto del widget.
 */
@Composable
internal fun ClassWidget(
    classData: ClassData,
    isSelected: Boolean,
    onClick: () -> Unit,
    delete: () -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * Tarjeta principal que contiene la información de la clase.
     *
     * @param onClick Acción que se ejecuta al hacer clic en la tarjeta.
     * @param modifier Modificador para personalizar el aspecto de la tarjeta.
     * @param elevation Elevación de la tarjeta en diferentes estados (default, pressed, hovered).
     * @param shape Forma de la tarjeta.
     * @param colors Colores de la tarjeta.
     */
    Card(
        onClick = onClick,
        modifier = modifier.size(width = 220.dp, height = 180.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = colorScheme.onBackground),
        border = if (isSelected) BorderStroke(3.dp, classData.color) else null
    ) {
        /**
         * Columna que organiza los elementos dentro de la tarjeta.
         *
         * @param modifier Modificador para personalizar el aspecto de la columna.
         * @param verticalArrangement Espaciado entre los elementos de la columna.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /**
             * Fila que contiene el nombre de la clase y el botón de eliminar.
             *
             * @param modifier Modificador para personalizar el aspecto de la fila.
             * @param verticalAlignment Alineación vertical de los elementos.
             * @param horizontalArrangement Espaciado horizontal entre los elementos.
             */
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = classData.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = classData.color
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
            /**
             * Divider para separar el nombre de la clase de los detalles.
             *
             * @param thickness Grosor de la línea divisoria.
             * @param color Color de la línea divisoria.
             */
            HorizontalDivider(thickness = 1.dp, color = colorScheme.outline.copy(alpha = 0.5f))
            Text(// Muestra la carrera y el grado de la clase.
                text = "${classData.career} - ${classData.degree}",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface
            )
            Text(
                text = "Section: ${classData.section}",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}