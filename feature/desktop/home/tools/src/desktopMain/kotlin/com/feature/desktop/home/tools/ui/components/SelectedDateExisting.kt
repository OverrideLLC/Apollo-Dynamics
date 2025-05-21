package com.feature.desktop.home.tools.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.home.tools.ui.screens.take_attendees.dateFormat
import com.override.data.utils.data.ClassData
import kotlinx.datetime.format
import org.koin.compose.viewmodel.koinViewModel

/**
 * Componente Composable que muestra las fechas disponibles para tomar asistencia.
 *
 * Muestra una lista horizontal de chips de filtro que representan las fechas en las que
 * se ha tomado asistencia. Permite al usuario seleccionar una fecha específica. Si no hay
 * fechas disponibles, muestra un mensaje indicando que no hay registros de asistencia.
 *
 * @param viewModel El ViewModel [TakeAttendeesViewModel] que gestiona el estado de la asistencia.
 * @param state El estado [TakeAttendeesViewModel.TakeAttendeesState] actual del ViewModel.
 * @param selectedClass La clase [ClassData] actualmente seleccionada.
 */
@Composable
internal fun SelectedDateExisting(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    selectedClass: ClassData?
) {
    // Si hay fechas disponibles, muestra la lista de fechas como chips de filtro.
    if (state.availableDates.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            // Itera sobre las fechas disponibles y crea un chip de filtro para cada una.
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
    // Si no hay fechas disponibles, muestra un mensaje informativo.
    } else {
        Text(
            "Aún no hay registros de asistencia para ${selectedClass?.name}. Haz clic en 'Pasar lista hoy' para comenzar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}