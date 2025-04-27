package com.feature.desktop.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.home.tools.screens.take_attendees.dateFormat
import kotlinx.datetime.format
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceSheet(
    state: TakeAttendeesViewModel.TakeAttendeesState,
    modifier: Modifier = Modifier,
    viewModel: TakeAttendeesViewModel = koinViewModel()
) {
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
            modifier = modifier.fillMaxWidth(),
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
            modifier = modifier.fillMaxWidth()
        )
    }
}