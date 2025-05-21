package com.feature.desktop.home.ai.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu // Usado para el menú de adjuntos
import androidx.compose.material.Icon // Usado para el icono de adjuntar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem // Usado para el menú de adjuntos
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.shared.resources.Res
import com.shared.resources.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

val knownServices = listOf(
    "Classroom ejecutar",
    "Classroom anunciar",
    "Classroom anuncios",
    "Classroom reporte",
    "Classroom subir tarea",
    "Classroom analizar trabajo",
    "ejecutar",
    "anunciar",
    "anuncios",
    "reporte",
    "subir tarea",
)

class ServiceTagVisualTransformation(
    private val services: List<String>,
    private val serviceStyle: SpanStyle
) : VisualTransformation {

    // Regex para encontrar "@servicio" seguido de un límite de palabra
    // Esto evita que "@classroomPlus" se marque si solo "classroom" es un servicio.
    private val regex = Regex("@(${services.joinToString("|")})\\b")

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val annotatedString = buildAnnotatedString {
            append(originalText) // Añade el texto original

            // Encuentra todas las coincidencias del regex
            regex.findAll(originalText).forEach { matchResult ->
                // Aplica el estilo al rango de la coincidencia
                addStyle(
                    style = serviceStyle,
                    start = matchResult.range.first,
                    end = matchResult.range.last + 1,
                )
            }
        }
        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}

@Composable
internal fun TextFieldAi(
    value: TextFieldValue,
    state: AiViewModel.AiState,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    onSend: (detectedService: String?, message: String) -> Unit,
    onAttachFile: () -> Unit,
    onAnotherAction: () -> Unit = {} // Asumo que esta acción no tiene texto visible directo aquí
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sendEnabled =
        (value.text.isNotBlank() || state.selectedFiles.isNotEmpty()) && !state.isLoading
    var attachMenuExpanded by remember { mutableStateOf(false) }

    var serviceMenuExpanded by remember { mutableStateOf(false) }
    var filteredServices by remember { mutableStateOf<List<String>>(emptyList()) }
    var mentionTriggerPosition by remember { mutableStateOf(-1) }

    val serviceTagStyle = SpanStyle(
        color = colorScheme.primary,
        background = colorScheme.primaryContainer,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp, // Mantener consistencia o ajustar según diseño
    )

    // El fontSize aquí es para cómo se *muestra* en el TextField, no en el dropdown.
    val serviceVisualTransformation = remember(knownServices, serviceTagStyle) {
        ServiceTagVisualTransformation(
            knownServices,
            serviceTagStyle.copy(fontSize = 20.sp)
        ) // Ajusta el tamaño de la etiqueta visual
    }

    var currentDetectedService by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(value.text) {
        val regex = Regex("@(${knownServices.joinToString("|")})\\b")
        val match = regex.find(value.text)
        currentDetectedService = match?.groups?.get(1)?.value
    }


    Row(
        modifier = modifier.fillMaxWidth().padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            IconButton(
                onClick = { attachMenuExpanded = true },
                modifier = Modifier
                    .size(55.dp)
                    .background(colorScheme.onSurface, shapes.medium)
            ) {
                Icon(
                    // Usando androidx.compose.material.Icon
                    painter = painterResource(Res.drawable.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "Abrir menú de adjuntos", // Ya estaba en español o es un buen punto de partida
                    tint = colorScheme.surface,
                )
            }
            DropdownMenu( // Usando androidx.compose.material.DropdownMenu
                expanded = attachMenuExpanded,
                onDismissRequest = { attachMenuExpanded = false }
            ) {
                DropdownMenuItem( // Usando androidx.compose.material3.DropdownMenuItem (asumiendo que se quiere M3 aquí también)
                    text = { Text("Adjuntar Archivo", fontSize = 20.sp) }, // Traducido
                    onClick = {
                        onAttachFile()
                        attachMenuExpanded = false
                    }
                )
                // Podrías añadir más items al menú aquí si fuera necesario
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = value,
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                onValueChange = { newValue ->
                    onValueChange(newValue)

                    val textUpToCursor = newValue.text.substring(0, newValue.selection.start)
                    val atSymbolIndex = textUpToCursor.lastIndexOf('@')

                    if (atSymbolIndex != -1) {
                        val query = textUpToCursor.substring(atSymbolIndex + 1)
                        if (!query.contains(" ") && query.length < 15) {
                            mentionTriggerPosition = atSymbolIndex
                            val suggestions = knownServices.filter {
                                it.startsWith(query, ignoreCase = true)
                            }
                            if (suggestions.isNotEmpty()) {
                                filteredServices = suggestions
                                serviceMenuExpanded = true
                            } else {
                                serviceMenuExpanded = false
                                filteredServices = emptyList()
                            }
                        } else {
                            serviceMenuExpanded = false
                        }
                    } else {
                        serviceMenuExpanded = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = shapes.medium,
                maxLines = 10,
                placeholder = {
                    Text(
                        text = "Escribe un mensaje o @servicio...",
                        fontSize = 20.sp
                    )
                }, // Traducido
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    focusedIndicatorColor = colorScheme.primary,
                    unfocusedIndicatorColor = colorScheme.primary.copy(alpha = 0.7f),
                    cursorColor = colorScheme.primary,
                    focusedTextColor = colorScheme.onSurface,
                    unfocusedTextColor = colorScheme.onSurface,
                    focusedPlaceholderColor = colorScheme.onSurface,
                    unfocusedPlaceholderColor = colorScheme.onSurface,
                ),
                visualTransformation = serviceVisualTransformation,
                trailingIcon = {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        IconButton(
                            onClick = {
                                val messageText = value.text.replace(
                                    Regex("@(${knownServices.joinToString("|")})\\b"),
                                    ""
                                ).trim()
                                onSend(currentDetectedService, messageText)
                            },
                            enabled = sendEnabled,
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = if (sendEnabled) colorScheme.primaryContainer else colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .hoverable(interactionSource = interactionSource)
                        ) {
                            Crossfade(
                                targetState = state.isLoading,
                                animationSpec = tween(300),
                                label = "SendIconCrossfade"
                            ) { isLoading ->
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = colorScheme.primary,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    androidx.compose.material3.Icon( // Especificando M3 Icon
                                        painter = painterResource(Res.drawable.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                        contentDescription = "Enviar mensaje", // Ya estaba en español
                                        tint = if (sendEnabled) colorScheme.primary else colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            )

            // DropdownMenu para autocompletar servicios
            // Asegúrate de usar consistentemente androidx.compose.material.DropdownMenu o androidx.compose.material3.DropdownMenu
            // Si OutlinedTextField es M3, y el menú de adjuntos usa M3, este también debería.
            // Sin embargo, tu código original usaba androidx.compose.material.DropdownMenu para adjuntos.
            // Mantendré la consistencia con el menú de adjuntos que usaba `material.DropdownMenu`
            // Si quieres cambiar a M3 para este también, asegúrate que `DropdownMenuItem` también sea de M3.
            DropdownMenu( // androidx.compose.material.DropdownMenu (o cambia a material3 si es necesario)
                expanded = serviceMenuExpanded && filteredServices.isNotEmpty(),
                onDismissRequest = { serviceMenuExpanded = false },
                modifier = Modifier.wrapContentWidth()
            ) {
                filteredServices.forEach { service ->
                    // Usando androidx.compose.material.DropdownMenuItem para consistencia con el menú de adjuntar,
                    // o androidx.compose.material3.DropdownMenuItem si se usa M3 DropdownMenu
                    androidx.compose.material.DropdownMenuItem( // o material3.DropdownMenuItem
                        // El texto aquí es "@nombreDelServicio", que es un identificador.
                        // No se traduce "service" en sí mismo a menos que los nombres de servicio sean localizables.
                        // Si "run" tuviera un alias en español como "ejecutar", se usaría eso.
                        // Por ahora, se muestra el nombre del servicio tal cual.
                        modifier = Modifier.width(400.dp), // Ajustar el ancho del item si es necesario
                        onClick = {
                            val currentText = value.text
                            val textBeforeMention = currentText.substring(0, mentionTriggerPosition)
                            val newText = "$textBeforeMention@$service "
                            val newCursorPosition = textBeforeMention.length + "@$service ".length

                            onValueChange(
                                TextFieldValue(
                                    text = newText,
                                    selection = TextRange(newCursorPosition)
                                )
                            )
                            serviceMenuExpanded = false
                        }
                    ) {
                        // El Text composable va dentro de DropdownMenuItem en Material (no M3)
                        Text("@$service", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}