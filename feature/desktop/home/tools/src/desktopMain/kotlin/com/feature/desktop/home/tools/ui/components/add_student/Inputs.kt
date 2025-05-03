package com.feature.desktop.home.tools.ui.components.add_student

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentState
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentViewModel

/**
 * Componente Composable que muestra los campos de entrada para agregar un nuevo estudiante.
 *
 * @param viewModel ViewModel para gestionar el estado y las acciones de la pantalla de agregar estudiante.
 * @param state Estado actual de la pantalla de agregar estudiante.
 */
@Composable
internal fun InputsAddStudent(
    viewModel: AddStudentViewModel,
    state: AddStudentState
) {
    Text("Add New Student", style = MaterialTheme.typography.headlineMedium) // Titulo del formulario
    // Campo de texto para el nombre del estudiante
    OutlinedTextField(
        value = state.studentName,
        onValueChange = viewModel::onStudentNameChange,
        label = { Text("Student Name") }, // Etiqueta del campo
        modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho disponible
        singleLine = true, // Solo permite una linea de texto
        isError = state.error != null && state.studentName.isBlank() // Muestra un error si el campo esta vacio y hay un error
    )

    /**
    * Campo de texto para el correo electrónico del estudiante.
    *
    * El correo electrónico es opcional.
    *
    * @param value El valor actual del correo electrónico.
    * @param onValueChange Lambda que se ejecuta cuando el valor del correo electrónico cambia.
    */
    OutlinedTextField(
        value = state.studentEmail,
        onValueChange = viewModel::onStudentEmailChange,
        label = { Text("Student Email (Optional)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    /**
    * Campo de texto para el número del estudiante.
    *
    * El número es opcional.
    *
    * @param value El valor actual del número.
    * @param onValueChange Lambda que se ejecuta cuando el valor del número cambia.
    */
    OutlinedTextField(
        value = state.studentNumber,
        onValueChange = viewModel::onStudentNumberChange,
        label = { Text("Student Number (Optional)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    /**
     * Campo de texto para el número de control del estudiante.
     *
     * El número de control es obligatorio.
     *
     * @param value El valor actual del número de control.
     * @param onValueChange Lambda que se ejecuta cuando el valor del número de control cambia.
     */
    OutlinedTextField(
        value = state.studentControlNumber,
        onValueChange = viewModel::onStudentControlNumberChange,
        label = { Text("Student Control Number") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}