package com.feature.desktop.home.tools.ui.components

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
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesViewModel
import com.override.data.utils.data.ClassData
import com.shared.resources.Res
import com.shared.resources.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Componente composable que muestra opciones para seleccionar la fecha y generar asistencia.
 *
 * @param viewModel ViewModel para gestionar la lógica de toma de asistencia.
 * @param state Estado actual del ViewModel, que incluye información sobre si se está cargando.
 * @param selectedClass Clase seleccionada, puede ser null si no se ha seleccionado ninguna.
 */
@Composable
internal fun SelectionDate(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    selectedClass: ClassData?
) {
    /**
     * Columna que contiene los botones para agregar un nuevo día de asistencia y para generar un QR de asistencia.
     *
     * @param modifier Modificador para personalizar el diseño de la columna.
     * @param verticalArrangement Define el espaciado vertical entre los elementos de la columna.
     * @param horizontalAlignment Define la alineación horizontal de los elementos en la columna.
     */
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { viewModel.addNewAttendanceDay() },
            /**
             * Define los colores del botón.
             *
             * @param containerColor Color de fondo del botón.
             * @param contentColor Color del contenido del botón (icono y texto).
             * @param disabledContainerColor Color de fondo del botón cuando está deshabilitado.
             * @param disabledContentColor Color del contenido del botón cuando está deshabilitado.
             */
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            ),
            enabled = !state.isLoading
            /**
             * Define si el botón está habilitado o deshabilitado basado en si se esta cargando el estado.
             */
        ) {
            /**
             * Muestra un icono de añadir.
             */
            Icon(Icons.Default.Add, contentDescription = "Add Today's Attendance")
            /**
             * Espacio entre el icono y el texto.
             */
            Spacer(Modifier.width(8.dp))
            Text("Pass List Today")
        }
        Button(
            onClick = { viewModel.generateAttendanceQr() },
            /**
             * Define los colores del botón.
             *
             * @param containerColor Color de fondo del botón.
             * @param contentColor Color del contenido del botón (icono y texto).
             * @param disabledContainerColor Color de fondo del botón cuando está deshabilitado.
             * @param disabledContentColor Color del contenido del botón cuando está deshabilitado.
             */
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            ),
            enabled = !state.isLoading
            /**
             * Define si el botón está habilitado o deshabilitado basado en si se esta cargando el estado.
             */
        ) {
            /**
             * Muestra un icono para el qr code.
             */
            Icon(
                painter = painterResource(Res.drawable.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                contentDescription = "Qr Attendance"
            )
            /**
             * Espacio entre el icono y el texto.
             */
            Spacer(Modifier.width(8.dp))
            Text("Qr Attendance")
        }
    }
}