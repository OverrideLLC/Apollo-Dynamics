package com.feature.desktop.home.components

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
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.home.tools.screens.take_attendees.dateFormat
import com.feature.desktop.home.utils.data.ClassData
import kotlinx.datetime.format
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectedDateExisting(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    selectedClass: ClassData?
) {
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
            "No attendance records yet for ${selectedClass?.name}. Click 'Pass List Today' to start.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}