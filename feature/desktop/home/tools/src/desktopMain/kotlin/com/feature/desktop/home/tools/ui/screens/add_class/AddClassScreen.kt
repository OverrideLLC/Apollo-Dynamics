package com.feature.desktop.home.tools.ui.screens.add_class

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.tools.ui.components.ColorChip
import org.koin.compose.viewmodel.koinViewModel

/**
 * Pantalla para añadir una nueva clase.
 *
 * Esta función Composable muestra una interfaz de usuario que permite al usuario añadir
 * una nueva clase con detalles como el nombre de la clase, grado/nivel, sección,
 * materia/carrera y un color asociado.
 *
 * @param viewModel El ViewModel que maneja la lógica de negocio y el estado de la pantalla.
 *                  Por defecto, utiliza un ViewModel proporcionado por Koin.
 * @param onCompletion Una función lambda que se llama cuando la clase se ha guardado con éxito.
 */
@Composable
internal fun AddClassScreen(
    viewModel: AddClassViewModel = koinViewModel(),
    onCompletion: () -> Unit
) {
    // Se obtiene el estado actual del ViewModel como un estado de Compose.
    val state by viewModel.state.collectAsState()

    // Efecto que se ejecuta cuando el estado de guardado exitoso cambia a true.
    LaunchedEffect(state.saveSuccess) { if (state.saveSuccess) onCompletion() }

    // Columna principal que organiza todos los elementos de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título de la pantalla.
        Text("Agregar Nueva Clase", style = MaterialTheme.typography.headlineMedium)

        // Campo de texto para el nombre de la clase.
        OutlinedTextField(
            value = state.className,
            onValueChange = viewModel::onClassNameChange,
            label = {
                Text("Nombre de la Clase")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.className.isBlank()
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = state.degree,
                onValueChange = viewModel::onDegreeChange,//
                label = {
                    Text("Grado/Nivel")
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error != null && state.degree.isBlank()
            )
            OutlinedTextField(
                value = state.section,
                onValueChange = viewModel::onSectionChange,//
                label = {
                    Text("Sección")
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.error != null && state.section.isBlank()
            )
        }
        // Campo de texto para la carrera/materia.
        OutlinedTextField(
            value = state.career,
            onValueChange = viewModel::onCareerChange,
            label = { Text("Carrera / Materia") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.career.isBlank()
        )

        // Selector de color.
        Text("Seleccionar Color:", style = MaterialTheme.typography.titleMedium)
        // Fila horizontal para mostrar las opciones de color.
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

        // Espacio entre el selector de color y los botones/mensajes.
        Spacer(Modifier.height(16.dp))

        // Contenedor para el botón de guardar, indicador de carga y mensajes de error/éxito.
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            // Columna para alinear verticalmente los elementos dentro del Box.
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Indicador de carga si el estado está en proceso de carga.
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    // Botón para guardar la clase.
                    Button(
                        onClick = viewModel::saveClass, enabled = !state.isLoading
                    ) {
                        Text("Guardar Clase", color = Color.White)
                    }
                }

                // Mensaje de error si existe un error en el estado.
                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                // Mensaje de éxito si la clase se guardó correctamente.
                if (state.saveSuccess) {
                    Text(
                        text = "¡Clase guardada exitosamente!",
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }
}