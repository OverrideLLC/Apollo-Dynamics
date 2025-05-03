package com.feature.desktop.home.tools.ui.screens.student_status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.screens.take_attendees.dateFormat
import com.override.data.utils.data.ClassData
import com.override.data.utils.enum.AttendanceStatus
import com.shared.resources.Res
import com.shared.resources.badge_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.call_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.contact_mail_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.engineering_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.format_color_fill_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.format_list_numbered_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.school_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.widgets_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import kotlinx.datetime.format
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentStatusScreen(
    id: String,
    viewModel: StudentStatusViewModel = koinViewModel()
) {
    viewModel.loadStudentStatus(id)
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Status") },
                // Puedes añadir un botón de navegación para volver atrás si es necesario
                // navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center // Centrar contenido de carga/error
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                state.student != null -> {
                    // Usamos LazyColumn para el contenido principal si el historial puede ser largo
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Sección de Información del Alumno
                        item {
                            StudentInfoCard(state.student!!) // !! es seguro por la comprobación previa
                        }

                        // Sección de Clase Asignada
                        item {
                            state.assignedClass?.let { classData ->
                                AssignedClassCard(classData)
                            } ?: Card(
                                modifier = Modifier.fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.surface)
                            ) {
                                Text(
                                    "No class assigned.",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Sección de Historial de Asistencia
                        item {
                            Text("Attendance History", style = MaterialTheme.typography.titleLarge)
                        }

                        if (state.attendanceHistory.isNotEmpty()) {
                            items(state.attendanceHistory) { record ->
                                AttendanceHistoryItem(record)
                            }
                        } else {
                            item {
                                Text(
                                    "No attendance records found.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Estado inesperado (ni cargando, ni error, ni datos)
                    Text("No student data available.", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

// --- Composables Auxiliares ---

@Composable
private fun StudentInfoCard(student: com.override.data.utils.data.Student) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(student.name, style = MaterialTheme.typography.bodyMedium)
            InfoRow(
                icon = Res.drawable.contact_mail_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = student.email
            )
            InfoRow(
                icon = Res.drawable.call_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = student.number
            ) // Asumiendo que 'number' es teléfono
            InfoRow(
                icon = Res.drawable.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
                text = "Control #: ${student.controlNumber}"
            )
            InfoRow(
                icon = Res.drawable.badge_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = "ID: ${student.id}"
            )
        }
    }
}

@Composable
private fun AssignedClassCard(classData: ClassData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Assigned Class", style = MaterialTheme.typography.titleLarge)
            InfoRow(
                icon = Res.drawable.school_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = classData.name
            )
            InfoRow(
                icon = Res.drawable.format_list_numbered_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = "Degree/Grade: ${classData.degree}"
            )
            InfoRow(
                icon = Res.drawable.widgets_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
                text = "Section: ${classData.section}"
            )
            InfoRow(
                icon = Res.drawable.engineering_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                text = "Career/Subject: ${classData.career}"
            )
            // Podrías mostrar el color de la clase también si quieres
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.format_color_fill_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "Class Color",
                    modifier = Modifier.size(20.dp),
                    tint = LocalContentColor.current.copy(alpha = 0.6f)
                )
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.size(16.dp).background(classData.color, CircleShape))
            }
        }
    }
}

@Composable
private fun AttendanceHistoryItem(record: AttendanceRecordItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.date.format(dateFormat),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Class: ${record.className}", // Muestra la clase del registro
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }
            Spacer(Modifier.width(16.dp))
            AttendanceStatusChip(record.status)
        }
    }
}

@Composable
private fun AttendanceStatusChip(status: AttendanceStatus) {
    val (text, color, icon) = when (status) {
        AttendanceStatus.PRESENT -> Triple(
            "Present",
            Color(0xFF4CAF50),
            Icons.Default.CheckCircle
        ) // Verde
        AttendanceStatus.ABSENT -> Triple("Absent", Color(0xFFF44336), Icons.Default.Close) // Rojo
        AttendanceStatus.TARDY -> Triple("Late", Color(0xFFFF9800), Icons.Default.Check) // Naranja
        AttendanceStatus.UNKNOWN -> Triple("Excused", Color(0xFF2196F3), Icons.Default.Info) // Azul
    }

    SuggestionChip(
        onClick = { /* No action needed for display chip */ },
        label = { Text(text) },
        icon = {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(SuggestionChipDefaults.IconSize)
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(),
        border = null
    )
}


@Composable
private fun InfoRow(icon: DrawableResource, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null, // Decorativo
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary // O un color más suave
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
