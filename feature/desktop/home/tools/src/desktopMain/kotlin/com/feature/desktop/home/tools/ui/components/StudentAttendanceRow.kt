package com.feature.desktop.home.tools.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus
import com.shared.ui.VerticalDivider
/**
 * Fila para mostrar la información de asistencia de un estudiante.
 *
 * Esta función composable muestra una fila con el número de índice, el nombre del estudiante
 * y chips de filtro para cambiar el estado de asistencia del estudiante.
 *
 * @param studentWithStatus Objeto que contiene la información del estudiante y su estado de asistencia actual.
 * @param index Índice del estudiante en la lista.
 * @param onClickListener Función que se llama cuando se hace clic en la fila.
 * @param onStatusChange Función que se llama cuando se cambia el estado de asistencia del estudiante.
 * Recibe como parámetro el nuevo estado de asistencia.
 * @param modifier Modificador para personalizar el diseño de la fila.
 */
@Composable
internal fun StudentAttendanceRow(
    studentWithStatus: StudentWithStatus,
    index: Int,
    onClickListener: () -> Unit,
    onStatusChange: (AttendanceStatus) -> Unit,
    /**
     * Modificador para personalizar el diseño de la fila.
     *
     * Por defecto, no se aplica ningún modificador adicional.
     */
    modifier: Modifier = Modifier
) {
    val student = studentWithStatus.student
    val currentStatus = studentWithStatus.status

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            .clickable { onClickListener() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        /**
         * Muestra el numero de lista del alumno
         *
         */
        Text(
            text = "${index + 1}",
            modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .fillMaxHeight(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        /**
         * separador vertical
         */
        VerticalDivider()
        Text(
            text = student.name,
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxHeight(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        /**
         * separador vertical
         */
        VerticalDivider()
        Row(
            modifier = Modifier
                .weight(4f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            /**
             * recorre la lista de status para generar los chip
             * se filtran los status unkown por que no se usan en este caso
             *
             */
            AttendanceStatus.entries.filter { it != AttendanceStatus.UNKNOWN }
                .forEach { statusOption ->
                    FilterChip(
                        selected = currentStatus == statusOption,
                        onClick = { onStatusChange(statusOption) },
                        label = { Text(statusOption.displayName) },
                        modifier = Modifier.height(32.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (statusOption) {
                                AttendanceStatus.PRESENT -> MaterialTheme.colorScheme.primaryContainer
                                AttendanceStatus.ABSENT -> MaterialTheme.colorScheme.errorContainer
                                AttendanceStatus.TARDY -> Color(0xFFFEF0C7)
                                AttendanceStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            selectedLabelColor = when (statusOption) {
                                AttendanceStatus.PRESENT -> MaterialTheme.colorScheme.onPrimaryContainer
                                AttendanceStatus.ABSENT -> MaterialTheme.colorScheme.onErrorContainer
                                AttendanceStatus.TARDY -> Color(0xFF935200)
                                AttendanceStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            selectedBorderColor = Color.Transparent,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            disabledSelectedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            selected = false,
                            enabled = true
                        )
                    )
                }
        }
    }
}