package com.feature.desktop.home.tools.ui.components.sheet

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.components.StudentAttendanceRow
import com.override.data.utils.data.StudentWithStatus
import com.override.data.utils.enum.AttendanceStatus
/**
 * Componente Composable que muestra una hoja de asistencia para una lista de estudiantes.
 *
 * @param studentsWithStatus Lista de estudiantes con su estado de asistencia.
 * @param onClickListener Función que se llama cuando se hace clic en un estudiante. Recibe el ID del estudiante.
 * @param onStudentStatusChange Función que se llama cuando el estado de asistencia de un estudiante cambia.
 *                              Recibe el ID del estudiante y el nuevo estado de asistencia.
 * @param modifier Modificador para personalizar el diseño del componente.
 */
@Composable
internal fun AttendanceSheetComponent(
    studentsWithStatus: List<StudentWithStatus>,
    onClickListener: (String) -> Unit,
    onStudentStatusChange: (studentId: String, newStatus: AttendanceStatus) -> Unit,
    /**
     * Componente Composable que muestra una hoja de asistencia para una lista de estudiantes.
     *
     * @param studentsWithStatus Lista de estudiantes con su estado de asistencia.
     * @param onClickListener Función que se llama cuando se hace clic en un estudiante. Recibe el ID del estudiante.
     * @param onStudentStatusChange Función que se llama cuando el estado de asistencia de un estudiante cambia.
     *                              Recibe el ID del estudiante y el nuevo estado de asistencia.
     * @param modifier Modificador para personalizar el diseño del componente.
     *
     * Este componente muestra una lista de estudiantes con su estado de asistencia. Si no hay estudiantes, muestra un mensaje.
     */
    modifier: Modifier = Modifier
) {
    if (studentsWithStatus.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se encontraron estudiantes para la fecha seleccionada.",
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

                // Lista de estudiantes
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = stateLazy
                ) {
                    itemsIndexed(
                        studentsWithStatus,
                        key = { _, sws -> sws.student.id }) { index, studentWithStatus -> // Usar el ID del estudiante como clave
                        StudentAttendanceRow(
                            studentWithStatus = studentWithStatus,
                            index = index,
                            onClickListener = { onClickListener(studentWithStatus.student.id) },
                            onStatusChange = { newStatus ->
                                onStudentStatusChange(studentWithStatus.student.id, newStatus)
                            }
                        )
                    }
                }
                // Barra de desplazamiento vertical
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState = stateLazy)
                )
            }
        }
    }
}
