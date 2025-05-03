package com.feature.desktop.home.tools.ui.components.sheet

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

/**
 * Componente Composable que muestra la hoja de asistencia para una fecha seleccionada.
 *
 * Este componente muestra un título con la fecha seleccionada, un indicador de carga
 * mientras se cargan los datos y una lista de estudiantes con sus respectivos estados
 * de asistencia. También permite ver el estado detallado de un estudiante específico.
 *
 * @param state El estado actual de la pantalla de toma de asistencia, que contiene la fecha
 * seleccionada, la lista de estudiantes y su estado, y un indicador de carga.
 * @param modifier Modificador para personalizar el diseño del componente.
 * @param viewModel El ViewModel para manejar las interacciones y la lógica de la pantalla.
 */
@Composable
internal fun AttendanceSheet(
    state: TakeAttendeesViewModel.TakeAttendeesState,
    modifier: Modifier = Modifier,
    viewModel: TakeAttendeesViewModel = koinViewModel()
) {
    // Formatea la fecha seleccionada para mostrarla en el título o muestra "No date selected" si no hay fecha seleccionada.
    val titleDate = state.selectedDate?.format(dateFormat) ?: "No date selected"
    // Estado para almacenar el ID del estudiante seleccionado, que se usa para mostrar la pantalla de detalle del estudiante.
    var idStudent by remember { mutableStateOf<String?>(null) }
    // Título principal de la hoja de asistencia con la fecha seleccionada.
    Text(
        text = "Attendance List - $titleDate", // Título con fecha
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    // Indicador de carga
    if (state.isLoading) {// Muestra un indicador de carga circular mientras se están cargando los datos.
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Muestra la lista de estudiantes con sus estados de asistencia.
        AttendanceSheetComponent(
            studentsWithStatus = state.studentsForSelectedDate,
            onStudentStatusChange = { studentId, newStatus ->
                // Actualiza el estado de asistencia del estudiante a través del ViewModel.
                viewModel.updateStudentAttendanceStatus(studentId, newStatus)
            },
            modifier = modifier.fillMaxWidth(),
            // Cuando se hace clic en un estudiante, se guarda su ID para mostrar su pantalla de detalle.
            onClickListener = { id -> idStudent = id }
        )
        // Si se ha seleccionado un estudiante (idStudent no es nulo), muestra la pantalla de detalle del estudiante.
        idStudent?.let {
            // Muestra un ScreenAction para la pantalla de detalle del estudiante.
            ScreenAction(
                // Título de la pantalla de detalle.
                // Icono para cerrar la pantalla de detalle.
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