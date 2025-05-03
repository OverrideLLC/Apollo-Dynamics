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
import com.shared.resources.LogoBlancoQuickness
import com.shared.resources.Res
import com.shared.ui.qr
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class) // Necesario para ExposedDropdownMenuBox
@Composable
internal fun AddStudentScreen(
    viewModel: AddStudentViewModel = koinViewModel(), // Inyecta el ViewModel
    onCompletion: () -> Unit // Callback para cuando se completa la acción
) {
    val state by viewModel.state.collectAsState()
    var isClassDropdownExpanded by remember { mutableStateOf(false) }

    // Navegar hacia atrás o cerrar cuando el guardado es exitoso
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
        Text("Add New Student", style = MaterialTheme.typography.headlineMedium)

        // Campo para el nombre del estudiante
        OutlinedTextField(
            value = state.studentName,
            onValueChange = viewModel::onStudentNameChange,
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.studentName.isBlank()
        )

        OutlinedTextField(
            value = state.studentEmail,
            onValueChange = viewModel::onStudentEmailChange,
            label = { Text("Student Email (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.studentNumber,
            onValueChange = viewModel::onStudentNumberChange,
            label = { Text("Student Number (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.studentControlNumber,
            onValueChange = viewModel::onStudentControlNumberChange,
            label = { Text("Student Control Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Selector de Clase
        Text("Assign to Class:", style = MaterialTheme.typography.titleMedium)

        if (state.isLoadingClasses) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        } else if (state.availableClasses.isNotEmpty()) {
            // Dropdown para seleccionar la clase
            ExposedDropdownMenuBox(
                expanded = isClassDropdownExpanded,
                onExpandedChange = { isClassDropdownExpanded = !isClassDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = state.selectedClass?.name
                        ?: "Select a class", // Muestra nombre o placeholder
                    onValueChange = {}, // No editable directamente
                    readOnly = true,
                    label = { Text("Class") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClassDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor() // Ancla el menú al TextField
                        .fillMaxWidth(),
                    isError = state.error != null && state.selectedClass == null
                )

                // Contenido del menú desplegable
                ExposedDropdownMenu(
                    expanded = isClassDropdownExpanded,
                    containerColor = colorScheme.primary,
                    onDismissRequest = { isClassDropdownExpanded = false }
                ) {
                    state.availableClasses.forEach { classItem ->
                        DropdownMenuItem(
                            text = { Text("${classItem.name} (${classItem.degree} ${classItem.section})", color = colorScheme.onTertiary) }, // Muestra detalles de la clase
                            onClick = {
                                viewModel.onClassSelected(classItem)
                                isClassDropdownExpanded = false // Cierra el menú
                            }
                        )
                    }
                }
            }
        } else {
            Text("No classes available to assign.", style = MaterialTheme.typography.bodyMedium)
        }


        Spacer(Modifier.height(16.dp)) // Espacio antes del botón

        // Botón de Guardar y Mensaje de Error/Carga
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.isLoadingSave) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = viewModel::saveStudent,
                        enabled = !state.isLoadingSave && state.selectedClass != null // Habilitado si no carga y hay clase seleccionada
                    ) {
                        Text("Save Student", color = colorScheme.onTertiary)
                    }
                }

                // Muestra mensaje de error general
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
                // No mostramos mensaje de éxito aquí porque usamos LaunchedEffect para navegar/cerrar
            }
        }
    }
}
