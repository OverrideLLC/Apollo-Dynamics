package com.feature.desktop.home.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus

// --- Componente para una Fila de Estudiante (Actualizado) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentAttendanceRow(
    // *** Cambiado a StudentWithStatus ***
    studentWithStatus: StudentWithStatus,
    index: Int,
    onClickListener: () -> Unit,
    onStatusChange: (AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    // Extraer el estudiante y su estado actual
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
        // Celda Número de Lista
        Text(
            text = "${index + 1}",
            modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .fillMaxHeight(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface // Usar color del tema
        )
        VerticalDivider()
        // Celda Nombre
        Text(
            text = student.name, // Usar student.name
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxHeight(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface // Usar color del tema
        )
        VerticalDivider()
        // Celda Estado (Chips)
        Row(
            modifier = Modifier
                .weight(4f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Filtrar UNKNOWN para los chips seleccionables
            AttendanceStatus.entries.filter { it != AttendanceStatus.UNKNOWN }
                .forEach { statusOption ->
                    FilterChip(
                        selected = currentStatus == statusOption, // Comparar con currentStatus
                        onClick = { onStatusChange(statusOption) }, // Llamar al callback con la nueva opción
                        label = { Text(statusOption.displayName) },
                        modifier = Modifier.height(32.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (statusOption) {
                                AttendanceStatus.PRESENT -> MaterialTheme.colorScheme.primaryContainer
                                AttendanceStatus.ABSENT -> MaterialTheme.colorScheme.errorContainer
                                AttendanceStatus.TARDY -> Color(0xFFFEF0C7) // Amarillo pálido (ajustar si es necesario)
                                AttendanceStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant // No se usa aquí
                            },
                            selectedLabelColor = when (statusOption) {
                                AttendanceStatus.PRESENT -> MaterialTheme.colorScheme.onPrimaryContainer
                                AttendanceStatus.ABSENT -> MaterialTheme.colorScheme.onErrorContainer
                                AttendanceStatus.TARDY -> Color(0xFF935200) // Texto oscuro para amarillo (ajustar si es necesario)
                                AttendanceStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant // No se usa aquí
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

// --- AttendanceSheetHeader (Sin cambios) ---
@Composable
fun AttendanceSheetHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min), // Ajustar altura al contenido
        verticalAlignment = Alignment.CenterVertically
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

// --- VerticalDivider (Sin cambios) ---
@Composable
fun RowScope.VerticalDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.fillMaxHeight().width(1.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
}


// --- Componente Principal de la Hoja de Asistencia (Actualizado) ---
@Composable
fun AttendanceSheet(
    // *** Cambiado a List<StudentWithStatus> ***
    studentsWithStatus: List<StudentWithStatus>,
    onClickListener: (String) -> Unit,
    onStudentStatusChange: (studentId: String, newStatus: AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    if (studentsWithStatus.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No students found for the selected date.", // Mensaje actualizado
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(modifier = modifier) {
            AttendanceSheetHeader()

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val stateLazy = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = stateLazy
                ) {
                    // *** Usar studentsWithStatus ***
                    itemsIndexed(
                        studentsWithStatus,
                        key = { _, sws -> sws.student.id }) { index, studentWithStatus ->
                        StudentAttendanceRow(
                            studentWithStatus = studentWithStatus, // Pasar el objeto combinado
                            index = index,
                            onClickListener = { onClickListener(studentWithStatus.student.id) },
                            onStatusChange = { newStatus ->
                                // Llamar al callback con el ID del estudiante y el nuevo estado
                                onStudentStatusChange(studentWithStatus.student.id, newStatus)
                            }
                        )
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = stateLazy)
                )
            }
        }
    }
}
