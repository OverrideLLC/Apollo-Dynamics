package com.feature.desktop.home.services.classroom.screen

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feature.desktop.home.services.classroom.utils.enums.ClassroomServices
import com.google.api.services.classroom.model.Course
import com.shared.resources.Res
import com.shared.resources.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.ui.ClassWidget
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClassroomAnnouncementScreen(
    announcement: String
) = Screen(announcement)

@Composable
internal fun Screen(
    announcement: String,
    viewModel: ClassroomAnnouncementViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.surface), // Fondo con gradiente sutil
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Padding general para la pantalla
            verticalArrangement = Arrangement.spacedBy(20.dp), // Espacio entre secciones principales
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la pantalla (Opcional, pero puede ayudar)
            Text(
                "Anuncios de Clase",
                style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.onSurface
            )

            ListCourses(
                state = state,
                onCourseClick = { course ->
                    viewModel.onCourseSelected(course)
                }
            )

            // Separador visual
            if (state.selectedCourseId != null) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = colorScheme.onSurface
                )
            }


            state.selectedCourseId?.let {
                RowListServices(
                    state = state,
                    viewModel = viewModel,
                    announcement = announcement,
                )
            }
        }
    }
}

@Composable
internal fun RowListServices(
    state: ClassroomAnnouncementState,
    viewModel: ClassroomAnnouncementViewModel,
    announcement: String,
) {
    var currentAnnouncementText by remember(announcement) { mutableStateOf(announcement) }

    Column( // Cambiado a Column para mejor organización vertical del título y el contenido
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Crear Anuncio y Acciones",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // Padding reducido para la fila interna
            verticalAlignment = Alignment.Top, // Alineado arriba para el label del TextField
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TextField para editar el anuncio
            OutlinedTextField(
                value = currentAnnouncementText,
                onValueChange = {
                    currentAnnouncementText = it
                },
                label = { Text("Escribe tu anuncio...", style = typography.bodySmall, color = colorScheme.onSurface) },
                modifier = Modifier
                    .weight(.5f) // Ocupa el espacio restante
                    .height(150.dp), // Altura fija para el campo de texto
                maxLines = 6,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true
                ),
                shape = shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = colorScheme.surface, // Un fondo sutil
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline,
                    textColor = colorScheme.onSurface,
                    cursorColor = colorScheme.primary
                )
            )

            // ListServices condicional
            if (state.servicesVisible) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxWidth(.5f)
                ) {
                    ListServices(
                        state = state,
                        viewModel = viewModel,
                        announcement = currentAnnouncementText
                    )
                }
            }
        }
    }
}

@Composable
internal fun ListServices(
    state: ClassroomAnnouncementState,
    announcement: String,
    viewModel: ClassroomAnnouncementViewModel
) {
    val scrollStateServices = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start), // Alineado al inicio
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            state = scrollStateServices
        ) {
            items(items = state.classroomServices) { service ->
                CardService(
                    icon = service.icon,
                    title = service.title,
                    onClick = {
                        state.selectedCourseId?.let { courseId ->
                            when (service) {
                                ClassroomServices.ADD_ANNOUNCE -> {
                                    viewModel.addAnnouncement(
                                        content = announcement,
                                        courseId = courseId
                                    )
                                }
                            }
                        }
                    },
                    size = 140.dp // Tamaño ligeramente reducido para mejor ajuste
                )
            }
        }
        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 4.dp),
            adapter = rememberScrollbarAdapter(
                scrollState = scrollStateServices
            )
        )
    }
}


@Composable
fun ListCourses(
    state: ClassroomAnnouncementState,
    onCourseClick: (Course) -> Unit
) {
    val scrollStateCourses = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) { // Envuelto en Column para añadir título
        Text(
            "Cursos Disponibles",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp) // Espacio debajo del título
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start), // Alineado al inicio
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp), // Padding para las tarjetas
                state = scrollStateCourses
            ) {
                state.courses?.let { courses ->
                    items(items = courses) { course ->
                        ClassWidget( // Asumiendo que ClassWidget ya tiene un buen diseño
                            name = course.name ?: "Clase sin nombre",
                            color = colorScheme.primary, // Usar color primario para el nombre
                            career = course.descriptionHeading ?: "",
                            degree = "",
                            section = course.section ?: "General",
                            onClick = { onCourseClick(course) },
                            delete = {},
                            isSelected = course.id == state.selectedCourseId,
                            isEnabledDeleted = false,
                            iconDeleted = Res.drawable.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
                            // Considera añadir un modifier aquí si necesitas ajustar el ClassWidget
                            // por ejemplo: modifier = Modifier.height(180.dp).width(220.dp)
                        )
                    }
                } ?: item { // Mostrar un mensaje si no hay cursos
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                        CircularProgressIndicator()
                    }
                }
            }
            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp), // Espacio entre tarjetas y scrollbar
                adapter = rememberScrollbarAdapter(
                    scrollState = scrollStateCourses
                )
            )
        }
    }
}

@Composable
internal fun CardService(
    size: Dp = 140.dp, // Ajustado el tamaño por defecto
    icon: DrawableResource,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp)) // Esquinas más redondeadas
            .border(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer // Un color de contenedor más elevado
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Añadir elevación
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = title,
                modifier = Modifier.size(size / 3f), // Icono un poco más grande
                tint = colorScheme.primary // Color primario para el icono
            )
            Spacer(modifier = Modifier.height(10.dp)) // Un poco más de espacio
            Text(
                text = title,
                style = typography.titleLarge.copy(fontWeight = FontWeight.Medium), // Estilo ajustado
                color = colorScheme.primary // Color de texto principal
            )
        }
    }
}
