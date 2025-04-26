package com.feature.desktop.home.tools.screens.take_attendees

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.AttendanceSheet // Importar componente actualizado
import com.feature.desktop.home.components.class_widget.ClassWidget
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TakeAttendeesScreen() = Screen()

@OptIn(FormatStringsInDatetimeFormats::class)
val dateFormat = LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }


@OptIn(ExperimentalMaterial3Api::class) // Para FilterChip
@Composable
private fun Screen(
    viewModel: TakeAttendeesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedClass = remember(state.selectedClassId, state.allClasses) {
        state.allClasses.find { it.id == state.selectedClassId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Ajustar espaciado
    ) {
        // --- Sección Selección de Clase ---
        Text("Select a Class:", style = MaterialTheme.typography.titleMedium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(state.allClasses, key = { it.id }) { classData ->
                ClassWidget(
                    classData = classData,
                    isSelected = classData.id == state.selectedClassId,
                    // *** Pasar el ID al ViewModel ***
                    onClick = { viewModel.selectClass(classData.id) }
                )
            }
        }

        HorizontalDivider()

        // --- Sección Selección de Fecha ---
        if (selectedClass != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Select Attendance Date:", style = MaterialTheme.typography.titleMedium)
                // Botón para añadir el día actual
                Button(
                    onClick = { viewModel.addNewAttendanceDay() },
                    enabled = !state.isLoading // Deshabilitar mientras carga
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Today's Attendance")
                    Spacer(Modifier.width(8.dp))
                    Text("Pass List Today") // "Pasar Lista Hoy"
                }
            }

            // Fila de Chips para seleccionar fechas existentes
            if (state.availableDates.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(state.availableDates, key = { it.toString() }) { date ->
                        FilterChip(
                            selected = date == state.selectedDate,
                            onClick = { viewModel.selectDate(date) },
                            label = { Text(date.format(dateFormat)) }, // Formatear fecha
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                enabled = true,
                                selected = false,
                            )
                        )
                    }
                }
            } else {
                Text(
                    "No attendance records yet for ${selectedClass.name}. Click 'Pass List Today' to start.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

            // --- Sección Hoja de Asistencia ---
            val titleDate = state.selectedDate?.format(dateFormat) ?: "No date selected"
            Text(
                text = "Attendance List - $titleDate", // Título con fecha
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Indicador de carga
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Pasar la lista correcta: state.studentsForSelectedDate
                AttendanceSheet(
                    studentsWithStatus = state.studentsForSelectedDate,
                    onStudentStatusChange = { studentId, newStatus ->
                        // La llamada al ViewModel no necesita cambiar aquí
                        viewModel.updateStudentAttendanceStatus(studentId, newStatus)
                    },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }

        } else {
            // Mensaje si no hay clase seleccionada
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Select a class to manage attendance.", // Mensaje actualizado
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
