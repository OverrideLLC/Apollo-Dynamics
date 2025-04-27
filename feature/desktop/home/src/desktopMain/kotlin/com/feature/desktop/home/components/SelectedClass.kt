package com.feature.desktop.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.class_widget.ClassWidget
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesViewModel
import com.shared.resources.Res
import com.shared.resources.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ScreenAction
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectedClass(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    modifier: Modifier
) {
    val scrollHorizontalState = rememberLazyListState()

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
                    .size(width = 220.dp, height = 180.dp),
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
                    modifier = Modifier.padding(16.dp).size(30.dp)
                )
            }
        }
        items(state.allClasses, key = { it.id }) { classData ->
            ClassWidget(
                classData = classData,
                isSelected = classData.id == state.selectedClassId,
                onClick = { viewModel.selectClass(classData.id) }
            )
        }
    }

    HorizontalScrollbar(
        modifier = modifier.fillMaxWidth(),
        adapter = rememberScrollbarAdapter(scrollHorizontalState)
    )
    ShowWindows(state = state)
}

@Composable
private fun ShowWindows(
    state: TakeAttendeesViewModel.TakeAttendeesState,
    viewModel: TakeAttendeesViewModel = koinViewModel()
) {
    state.qr?.let { qr ->
        ScreenAction(
            size = DpSize(
                width = 500.dp,
                height = 500.dp
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