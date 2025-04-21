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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.shared.resources.Res
import com.shared.resources.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun TextFieldAi(
    value: String,
    state: AiViewModel.AiState,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachFile: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sendEnabled = (value.isNotBlank() || state.selectedFiles.isNotEmpty()) && !state.isLoading

    Row(
        modifier = modifier.fillMaxWidth().padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onAttachFile,
            modifier = Modifier
                .size(48.dp)
                .background(colorScheme.onTertiary, CircleShape)
        ) {
            Icon(
                painter = painterResource(Res.drawable.attach_file_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                contentDescription = "Adjuntar archivo",
                tint = colorScheme.primary
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(24.dp),
            maxLines = 5,
            placeholder = { Text(text = "Escribe tu mensaje...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.onTertiary,
                unfocusedContainerColor = colorScheme.onTertiary,
                focusedIndicatorColor = colorScheme.primary,
                unfocusedIndicatorColor = colorScheme.primary,
                cursorColor = colorScheme.primary,
                focusedTextColor = colorScheme.tertiary,
                unfocusedTextColor = colorScheme.tertiary,
                focusedPlaceholderColor = colorScheme.primary,
                unfocusedPlaceholderColor = colorScheme.primary,
            ),
            trailingIcon = {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    IconButton(
                        onClick = onSend,
                        enabled = sendEnabled,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (value.isNotBlank() && !state.isLoading) colorScheme.onTertiary else colorScheme.onTertiary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .hoverable(interactionSource = interactionSource) // Añadir hoverable
                    ) {
                        Crossfade(
                            targetState = state.isLoading,
                            animationSpec = tween(300)
                        ) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    painter = painterResource(Res.drawable.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                    contentDescription = "Send message...",
                                    tint = if (value.isNotBlank() && !state.isLoading) colorScheme.primary else colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
