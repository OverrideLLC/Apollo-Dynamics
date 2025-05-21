package com.feature.desktop.home.tools.ui.components

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
import com.feature.desktop.home.tools.ui.screens.add_class.AddClassScreen
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesViewModel
import com.shared.resources.LogoBlancoQuickness
import com.shared.resources.Res
import com.shared.resources.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ClassWidget
import com.shared.ui.ScreenAction
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable que muestra la sección para seleccionar una clase.
 *
 * Esta función muestra una lista horizontal de clases disponibles,
 * permite al usuario seleccionar una clase y también añadir nuevas clases.
 * Además, muestra ventanas modales para visualizar el código QR de asistencia
 * y para crear una nueva clase.
 *
 * @param viewModel El ViewModel que gestiona el estado de la toma de asistencia.
 * @param state El estado actual del ViewModel.
 * @param modifier Modificador para personalizar el diseño del composable.
 */
@Composable
internal fun SelectedClass(
    viewModel: TakeAttendeesViewModel = koinViewModel(),
    state: TakeAttendeesViewModel.TakeAttendeesState,
    modifier: Modifier
) {
    // Estado para el desplazamiento horizontal de la lista de clases.
    val scrollHorizontalState = rememberLazyListState()

    Text(
        "Selecciona una Clase:",
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        // Se asigna el estado del desplazamiento horizontal.
        state = scrollHorizontalState
    ) {
        item {
            Card(
                onClick = { viewModel.addNewClass() },
                modifier = Modifier.size(width = 220.dp, height = 180.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp,
                    hoveredElevation = 6.dp
                ),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(3.dp, Color.Black),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_circle_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "Add Class",
                    modifier = Modifier.padding(16.dp).size(30.dp)
                )
            }
        }
        // Se muestran las clases existentes.
        items(state.allClasses, key = { it.id }) { classData ->
            ClassWidget(
                name = classData.name,
                color = classData.color,
                career = classData.career,
                degree = classData.degree,
                section = classData.section,
                isSelected = classData.id == state.selectedClassId,
                onClick = { viewModel.selectClass(classData.id) },
                delete = { viewModel.deletedClass(classData.id) },
                iconDeleted = Res.drawable.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
            )
        }
    }

    // Barra de desplazamiento horizontal.
    HorizontalScrollbar(
        modifier = modifier.fillMaxWidth(),
        adapter = rememberScrollbarAdapter(scrollHorizontalState)
    )
    // Muestra la ventana modal con el código QR si está disponible.
    state.qr?.let { qr ->
        ShowWindows(
            name = "Asistencia por QR",
            icon = Res.drawable.qr_code_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
            dpSize = DpSize(
                width = 500.dp,
                height = 500.dp
            ),
            close = {
                viewModel.closeQr()
            },
            content = {
                Image(
                    painter = qr,
                    contentDescription = "Asistencia por QR",
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
    // Muestra la ventana modal para crear una nueva clase si se ha solicitado.
    state.newClass?.let {
        ShowWindows(
            name = "Nueva Clase",
            dpSize = DpSize(
                width = 1200.dp,
                height = 720.dp,
            ),
            close = {
                viewModel.closeNewClass()
            },
            content = {
                AddClassScreen(
                    onCompletion = {
                        viewModel.closeNewClass()
                    }
                )
            }
        )
    }
}

/**
 * Composable privado que muestra una ventana modal genérica.
 *
 * @param nombre El nombre de la ventana.
 * @param icon El icono de la ventana.
 * @param dpSize El tamaño de la ventana.
 * @param close La función que se ejecuta al cerrar la ventana.
 * @param content El contenido que se mostrará en la ventana.
 *
 */
@Composable
private fun ShowWindows(
    name: String = "",
    icon: DrawableResource? = null,
    dpSize: DpSize,
    close: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    ScreenAction(
        size = dpSize,
        name = name,
        icon = icon ?: Res.drawable.LogoBlancoQuickness,
        close = { close() },
        content = {
            content()
        }
    )
}