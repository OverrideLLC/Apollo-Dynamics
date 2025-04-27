package com.feature.desktop.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.home.utils.data.ClassData
import com.shared.resources.Res
import com.shared.resources.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectionDate(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    selectedClass: ClassData?
) {
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
}