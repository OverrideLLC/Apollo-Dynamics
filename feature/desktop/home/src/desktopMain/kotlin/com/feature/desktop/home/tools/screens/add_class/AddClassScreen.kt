package com.feature.desktop.home.tools.screens.add_class

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AddClassScreen(
    viewModel: AddClassViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Mostrar mensaje de éxito si aplica
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            // Aquí podrías mostrar un Snackbar o navegar hacia atrás
            println("¡Clase guardada exitosamente!")
            // Por ahora, solo imprimimos en consola.
            // Considera añadir un callback onSaveSuccess: () -> Unit a AddClassScreen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add New Class", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.className,
            onValueChange = viewModel::onClassNameChange,
            label = { Text("Class Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.className.isBlank() // Marcar error si está vacío y hay error general
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = state.degree,
                onValueChange = viewModel::onDegreeChange,
                label = { Text("Degree/Grade") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error != null && state.degree.isBlank()
            )
            OutlinedTextField(
                value = state.section,
                onValueChange = viewModel::onSectionChange,
                label = { Text("Section") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error != null && state.section.isBlank()
            )
        }

        OutlinedTextField(
            value = state.career,
            onValueChange = viewModel::onCareerChange,
            label = { Text("Career / Subject") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.career.isBlank()
        )

        // Selector de Color
        Text("Select Color:", style = MaterialTheme.typography.titleMedium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(state.availableColors) { color ->
                ColorChip(
                    color = color,
                    isSelected = color == state.selectedColor,
                    onClick = { viewModel.onColorSelected(color) }
                )
            }
        }

        Spacer(Modifier.height(16.dp)) // Espacio antes del botón

        // Botón de Guardar y Mensaje de Error/Carga
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = viewModel::saveClass,
                        enabled = !state.isLoading // Deshabilitar si está cargando
                    ) {
                        Text("Save Class")
                    }
                }

                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (state.saveSuccess) {
                    Text(
                        text = "Class saved successfully!",
                        color = Color(0xFF4CAF50), // Verde éxito
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// Composable simple para mostrar un chip de color seleccionable
@Composable
fun ColorChip(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.outlineVariant else Color.Gray.copy(
                    alpha = 0.5f
                ),
                shape = CircleShape
            )
    )
}

