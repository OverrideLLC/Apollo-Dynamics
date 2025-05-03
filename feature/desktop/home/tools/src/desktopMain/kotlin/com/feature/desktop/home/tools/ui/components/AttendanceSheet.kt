package com.feature.desktop.home.tools.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.screens.student_status.StudentStatusScreen
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.home.tools.ui.screens.take_attendees.dateFormat
import com.shared.resources.Res
import com.shared.resources.school_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import kotlinx.datetime.format
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceSheet(
    state: TakeAttendeesViewModel.TakeAttendeesState,
    modifier: Modifier = Modifier,
    viewModel: TakeAttendeesViewModel = koinViewModel()
) {
    val titleDate = state.selectedDate?.format(dateFormat) ?: "No date selected"
    var idStudent by remember { mutableStateOf<String?>(null) }
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
            modifier = modifier.fillMaxWidth(),
            onClickListener = { id -> idStudent = id }
        )
        idStudent?.let {
            ScreenAction(
                name = "Student Status",
                icon = Res.drawable.school_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                close = { idStudent = null },
                content = {
                    StudentStatusScreen(id = it)
                }
            )
        }
    }
}