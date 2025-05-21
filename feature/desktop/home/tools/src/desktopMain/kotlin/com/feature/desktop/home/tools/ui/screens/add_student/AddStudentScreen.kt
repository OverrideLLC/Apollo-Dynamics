package com.feature.desktop.home.tools.ui.screens.add_student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.components.add_student.AssignClass
import com.feature.desktop.home.tools.ui.components.add_student.InputsAddStudent
import com.shared.resources.LogoBlancoQuickness
import com.shared.resources.Res
import com.shared.ui.qr
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AddStudentScreen(
    viewModel: AddStudentViewModel = koinViewModel(),
    onCompletion: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isClassDropdownExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            onCompletion()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputsAddStudent(
            viewModel = viewModel,
            state = state
        )
        AssignClass(
            state = state,
            viewModel = viewModel,
            isClassDropdownExpanded = isClassDropdownExpanded,
            onClassDropdownExpandedChange = { isClassDropdownExpanded = it }
        )
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.isLoadingSave) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = viewModel::saveStudent,
                        enabled = !state.isLoadingSave && state.selectedClass != null
                    ) {
                        Text("Save Student", color = colorScheme.onTertiary)
                    }
                }
                state.error?.let {
                    Text(
                        text = it,
                        color = colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                state.selectedClass?.let { classItem ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Black,
                                shape = shapes.small
                            )
                            .size(200.dp),
                        contentAlignment = Alignment.Center,
                        content = {
                            Image(
                                painter = qr(token = classItem.id, icon = Res.drawable.LogoBlancoQuickness),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(.9f)
                            )
                        }
                    )
                }
            }
        }
    }
}
