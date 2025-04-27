package com.feature.desktop.home.tools.screens.take_attendees

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.AttendanceSheet
import com.feature.desktop.home.components.SelectedClass
import com.feature.desktop.home.components.SelectedDateExisting
import com.feature.desktop.home.components.SelectionDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TakeAttendeesScreen() = Screen()

@OptIn(FormatStringsInDatetimeFormats::class)
val dateFormat = LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }


@OptIn(ExperimentalMaterial3Api::class)
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
        SelectedClass(modifier = Modifier.align(Alignment.Start), state = state)
        HorizontalDivider()
        selectedClass?.let {
            SelectionDate(state = state, selectedClass = selectedClass)
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            SelectedDateExisting(state = state, selectedClass = selectedClass)
            AttendanceSheet(state = state, modifier = Modifier.weight(1f))
        } ?: run {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Select a class to manage attendance.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}