package com.feature.desktop.home.tools.ui.components.add_student

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentState
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentViewModel

/**
 * Composable que permite asignar un estudiante a una clase.
 *
 * @param state Estado actual de la pantalla de añadir estudiante.
 * @param viewModel ViewModel para gestionar la lógica de la pantalla de añadir estudiante.
 * @param isClassDropdownExpanded Indica si el menú desplegable de clases está expandido.
 * @param onClassDropdownExpandedChange Callback para notificar el cambio de estado del menú desplegable.
 *
 * Este componente muestra un menú desplegable con las clases disponibles para asignar al estudiante.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AssignClass(
    state: AddStudentState,
    viewModel: AddStudentViewModel,
    isClassDropdownExpanded: Boolean,
    onClassDropdownExpandedChange: (Boolean) -> Unit
) {
    // Título para indicar la sección de asignación de clase.
    Text("Assign to Class:", style = MaterialTheme.typography.titleMedium)

    // Muestra un indicador de carga mientras se cargan las clases.
    if (state.isLoadingClasses) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
        // Si hay clases disponibles, muestra el menú desplegable.
    } else if (state.availableClasses.isNotEmpty()) {
        ExposedDropdownMenuBox(
            expanded = isClassDropdownExpanded,
            // Invierte el estado del menú desplegable cuando se expande o colapsa.
            onExpandedChange = { onClassDropdownExpandedChange(!isClassDropdownExpanded) }
        ) {
            // Campo de texto que muestra la clase seleccionada o un mensaje de selección.
            OutlinedTextField(
                value = state.selectedClass?.name
                    ?: "Select a class",
                // El campo no es editable directamente.
                onValueChange = {},
                readOnly = true,
                label = { Text("Class") }, // Etiqueta del campo de texto.
                // Icono desplegable al final del campo de texto.
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClassDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor() // Ancla el menú desplegable al campo de texto.
                    .fillMaxWidth(),
                // Indica si hay un error (no se ha seleccionado una clase)
                isError = state.error != null && state.selectedClass == null
            )

            /**
             * Menú desplegable que muestra las clases disponibles.
             *
             * @param expanded Indica si el menú está expandido.
             * @param onDismissRequest Callback para cuando se cierra el menú.
             */
            ExposedDropdownMenu(
                expanded = isClassDropdownExpanded,
                containerColor = colorScheme.primary,
                onDismissRequest = { onClassDropdownExpandedChange(false) }
            ) {
                state.availableClasses.forEach { classItem ->
                    DropdownMenuItem(
                        // Muestra el nombre, grado y sección de la clase.
                        text = { Text("${classItem.name} (${classItem.degree} ${classItem.section})", color = colorScheme.onTertiary) },
                         // Cuando se selecciona una clase.
                        onClick = {
                            // Se llama al ViewModel para guardar la clase seleciconada.
                            viewModel.onClassSelected(classItem)
                            // Se cierra el menu.
                            onClassDropdownExpandedChange(false)
                        }
                    )
                }
            }
        }
    } else { // Si no hay clases disponibles, muestra un mensaje.
        Text("No classes available to assign.", style = MaterialTheme.typography.bodyMedium)
    }
}