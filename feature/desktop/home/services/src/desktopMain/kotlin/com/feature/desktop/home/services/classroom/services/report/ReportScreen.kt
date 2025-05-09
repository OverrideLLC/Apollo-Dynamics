package com.feature.desktop.home.services.classroom.services.report


import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.api.services.classroom.model.Course
import com.shared.resources.Res
import com.shared.resources.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ClassWidget
import org.koin.compose.viewmodel.koinViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.util.Locale

// Implementación actual para mostrar un selector de archivos para guardar en Desktop
// Actual implementation to show a file save picker on Desktop
fun showFileSavePicker(title: String, allowedExtensions: List<String>): String? {
    val dialog = FileDialog(null as Frame?, title, FileDialog.SAVE)

    // Configurar filtro de archivos si hay extensiones permitidas
    // Set file filter if there are allowed extensions
    if (allowedExtensions.isNotEmpty()) {
        dialog.setFilenameFilter { _, name ->
            allowedExtensions.any { name.endsWith(".$it", ignoreCase = true) }
        }
    }

    dialog.isVisible = true // Muestra el diálogo de forma modal

    // Obtener el directorio y el nombre del archivo seleccionados
    // Get the selected directory and file name
    val directory = dialog.directory
    val file = dialog.file

    // Construir la ruta completa del archivo
    // Construct the full file path
    return if (directory != null && file != null) {
        // Asegurarse de que la extensión .pdf esté presente si se seleccionó
        // Ensure the .pdf extension is present if selected
        val fileNameWithExtension =
            if (allowedExtensions.contains("pdf") && file.lowercase(Locale.getDefault())
                    .endsWith(".pdf")
            ) {
                "$file.pdf"
            } else {
                file
            }
        "$directory$fileNameWithExtension"
    } else {
        null // El usuario canceló el diálogo
    }
}

@Composable
fun ReportScreen(
    viewModel: ReportViewmodel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    // Efecto lateral para mostrar el selector de archivos cuando sea necesario
    // Side effect to show the file picker when needed
    LaunchedEffect(uiState.isFilePickerVisible) {
        if (uiState.isFilePickerVisible) {
            val filePath = showFileSavePicker(
                title = "Guardar Reporte PDF",
                allowedExtensions = listOf("pdf")
            )
            // Llama al ViewModel con la ruta seleccionada o null si se canceló
            // Call the ViewModel with the selected path or null if canceled
            viewModel.hideFilePicker() // Oculta el selector independientemente del resultado
            if (filePath != null) {
                viewModel.startReportGeneration(
                    uiState.selectedCourseId ?: "",
                    showFilePicker = false,
                    filePath = filePath
                )
            } else {
                // Manejar la cancelación del selector de archivos si es necesario
                // Handle file picker cancellation if needed
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListCourses(
            state = uiState,
            onCourseClick = {
                viewModel.selectCourse(it.id)
            }
        )
        HorizontalDivider()
        Text("Reporte de Alumnos", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Inicia el proceso de generación del reporte mostrando el selector de archivos
                // Start the report generation process by showing the file picker
                viewModel.startReportGeneration(
                    uiState.selectedCourseId ?: "",
                    showFilePicker = true
                )
            },
            enabled = !uiState.isLoading // Deshabilita el botón mientras carga
        ) {
            Text("Generar Reporte PDF")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar indicador de carga
        // Show loading indicator
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Text("Generando reporte...")
        }

        // Mostrar mensaje de éxito o error
        // Show success or error message
        uiState.error?.let { errorMessage ->
            Text("Error: $errorMessage", color = MaterialTheme.colors.error)
        }

        if (uiState.pdfGenerated) {
            Text("Reporte PDF generado con éxito en: ${uiState.pdfFilePath}")
        }

        // Opcional: Mostrar un resumen del reporte en la pantalla (si uiState.report no es null)
        // Optional: Display a summary of the report on the screen (if uiState.report is not null)
        uiState.report?.let { reportData ->
            // Aquí podrías mostrar una vista previa o resumen del reporte en la UI
            // You could display a preview or summary of the report here in the UI
            // Text("Reporte para ${reportData.courseName}: ${reportData.studentReports.size} alumnos")
        }
    }
}


@Composable
private fun ListCourses(
    state: ReportUiState,
    onCourseClick: (Course) -> Unit
) {
    val scrollStateCourses = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Available Courses",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.Start
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
                state = scrollStateCourses
            ) {
                state.courses?.let { courses ->
                    items(items = courses) { course ->
                        ClassWidget(
                            name = course.name ?: "No name",
                            color = colorScheme.primary,
                            career = course.descriptionHeading ?: "",
                            degree = "",
                            section = course.section ?: "General",
                            onClick = { onCourseClick(course) },
                            delete = {},
                            isSelected = course.id == state.selectedCourseId,
                            isEnabledDeleted = false,
                            iconDeleted = Res.drawable.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
                        )
                    }
                } ?: item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp).padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
            }
            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp),
                adapter = rememberScrollbarAdapter(
                    scrollState = scrollStateCourses
                )
            )
        }
    }
}
