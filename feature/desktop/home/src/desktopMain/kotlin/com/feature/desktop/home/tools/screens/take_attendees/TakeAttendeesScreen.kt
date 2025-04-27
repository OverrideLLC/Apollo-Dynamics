package com.feature.desktop.home.tools.screens.take_attendees

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.AttendanceSheet
import com.feature.desktop.home.components.class_widget.ClassWidget
import com.shared.resources.Res
import com.shared.resources.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.painterResource
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

    val scrollHorizontalState = rememberLazyListState()

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
            contentPadding = PaddingValues(vertical = 4.dp),
            state = scrollHorizontalState
        ) {
            item {
                Card(
                    onClick = { viewModel.addNewClass() },
                    modifier = Modifier
                        .size(width = 220.dp, height = 180.dp), // Tamaño ligeramente ajustado
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp,
                        hoveredElevation = 6.dp
                    ),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    border = BorderStroke(3.dp, Color.Black),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Class",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            items(state.allClasses, key = { it.id }) { classData ->
                ClassWidget(
                    classData = classData,
                    isSelected = classData.id == state.selectedClassId,
                    // *** Pasar el ID al ViewModel ***
                    onClick = { viewModel.selectClass(classData.id) }
                )
            }
        }

        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            adapter = rememberScrollbarAdapter(scrollHorizontalState)
        )

        HorizontalDivider()

        // --- Sección Selección de Fecha ---
        if (selectedClass != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    onClick = { viewModel.addNewAttendanceDay() },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    ),
                    enabled = !state.isLoading
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Today's Attendance")
                    Spacer(Modifier.width(8.dp))
                    Text("Pass List Today")
                }
                Button(
                    onClick = { viewModel.generateAttendanceQr() },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    ),
                    enabled = !state.isLoading
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Qr Attendance"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Qr Attendance")
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
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
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
    state.qr?.let { qr ->
        ScreenAction(
            size = DpSize(
                width = 256.dp,
                height = 256.dp
            ),
            name = "Qr Attendance",
            icon = Res.drawable.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
            close = { viewModel.closeQr() },
            content = {
                Image(
                    painter = qr,
                    contentDescription = "Qr Attendance",
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}
