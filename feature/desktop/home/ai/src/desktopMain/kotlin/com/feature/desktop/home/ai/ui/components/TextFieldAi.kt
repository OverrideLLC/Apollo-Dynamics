package com.feature.desktop.home.ai.ui.components

// import androidx.compose.foundation.layout.wrapContentWidth // No es necesario explícitamente aquí
// import androidx.compose.ui.text.PlatformTextStyle // No estaba siendo usado
// import com.shared.resources.menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24 // No estaba siendo usado
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
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

// Lista de servicios conocidos (puedes obtenerla de tu ViewModel o una constante)
val knownServices = listOf(
    "run",
    "classroom announce",
    "classroom announcements",
    "classroom report",
    "classroom update assignment"
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
    onAnotherAction: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sendEnabled =
        (value.text.isNotBlank() || state.selectedFiles.isNotEmpty()) && !state.isLoading
    var attachMenuExpanded by remember { mutableStateOf(false) } // Renombrado para claridad

    // --- Estados para el DropdownMenu de autocompletado de servicios ---
    var serviceMenuExpanded by remember { mutableStateOf(false) }
    var filteredServices by remember { mutableStateOf<List<String>>(emptyList()) }
    // currentMentionQuery no es estrictamente necesario si filtramos directamente
    var mentionTriggerPosition by remember { mutableStateOf(-1) }
    // --- Fin de estados para autocompletado ---

    val serviceTagStyle = SpanStyle(
        color = colorScheme.primary,
        background = colorScheme.primaryContainer,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
    )

    val serviceVisualTransformation = remember(knownServices, serviceTagStyle) {
        ServiceTagVisualTransformation(knownServices, serviceTagStyle.copy(fontSize = 20.sp))
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
        // Menú para adjuntar archivos (como en tu código)
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
                    contentDescription = "Abrir menú de adjuntos",
                    tint = colorScheme.surface,
                )
            }
            DropdownMenu( // Usando androidx.compose.material.DropdownMenu
                expanded = attachMenuExpanded,
                onDismissRequest = { attachMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Attach File", fontSize = 20.sp) },
                    onClick = {
                        onAttachFile()
                        attachMenuExpanded = false
                    }
                )
            }
        }

        // TextField principal con DropdownMenu de autocompletado anclado
        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = value,
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                onValueChange = { newValue ->
                    onValueChange(newValue) // Propagar el cambio

                    val textUpToCursor = newValue.text.substring(0, newValue.selection.start)
                    val atSymbolIndex = textUpToCursor.lastIndexOf('@')

                    if (atSymbolIndex != -1) {
                        val query = textUpToCursor.substring(atSymbolIndex + 1)
                        // Asegurarse de que no haya espacios entre @ y la query para activar el menú
                        if (!query.contains(" ") && query.length < 15) { // Limitar longitud de query para evitar búsquedas largas
                            mentionTriggerPosition = atSymbolIndex
                            val suggestions = knownServices.filter {
                                it.startsWith(query, ignoreCase = true)
                            }
                            if (suggestions.isNotEmpty()) {
                                filteredServices = suggestions
                                serviceMenuExpanded = true
                            } else {
                                serviceMenuExpanded = false
                                filteredServices = emptyList() // Limpiar si no hay sugerencias
                            }
                        } else {
                            serviceMenuExpanded = false // Espacio después de @ o query muy larga
                        }
                    } else {
                        serviceMenuExpanded = false // No hay @ reciente
                    }
                },
                modifier = Modifier.fillMaxWidth(), // El focusRequester puede ser añadido si se necesita
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = shapes.medium,
                maxLines = 10,
                placeholder = { Text(text = "Type a message or @service...", fontSize = 20.sp) },
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
                                // Considerar limpiar el campo aquí si es el comportamiento deseado
                                // onValueChange(TextFieldValue(""))
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
                                        contentDescription = "Enviar mensaje",
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
            // Usando la misma combinación de importaciones que tu menú de adjuntar
            DropdownMenu( // androidx.compose.material.DropdownMenu
                expanded = serviceMenuExpanded && filteredServices.isNotEmpty(),
                onDismissRequest = { serviceMenuExpanded = false },
                modifier = Modifier.wrapContentWidth() // Ajustar ancho para que no sea pantalla completa
                // Puedes usar .wrapContentWidth() si prefieres que se ajuste al contenido
            ) {
                filteredServices.forEach { service ->
                    DropdownMenuItem( // androidx.compose.material3.DropdownMenuItem
                        text = {
                            Text("@$service", fontSize = 20.sp)
                        },
                        modifier = Modifier.width(400.dp),
                        onClick = {
                            val currentText = value.text
                            // val selectionStart = value.selection.start // No se usa directamente aquí
                            val textBeforeMention = currentText.substring(0, mentionTriggerPosition)
                            // El texto después de la mención actual (lo que se está escribiendo) se descarta
                            // y se reemplaza con el servicio completo + espacio.
                            // Si había texto después de la query de mención, se necesita una lógica más compleja para conservarlo.
                            // Esta implementación simple reemplaza desde el @.

                            val newText =
                                "$textBeforeMention@$service " // Añade un espacio después del servicio
                            val newCursorPosition = textBeforeMention.length + "@$service ".length

                            onValueChange(
                                TextFieldValue(
                                    text = newText,
                                    selection = TextRange(newCursorPosition)
                                )
                            )
                            serviceMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}
