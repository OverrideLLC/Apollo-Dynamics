package com.feature.desktop.home.ai

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.components.MarkdownText
import com.shared.resources.Res
import com.shared.resources.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AiScreen() = Screen()

@Composable
private fun Screen(
    viewModel: AiViewModel = koinViewModel()
) {
    val state by remember { viewModel.state }.collectAsState()
    val messages = state.messages
    val newMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Chat(messages, modifier = Modifier.weight(.9f))
        TextFieldAi(
            value = newMessage.value,
            onValueChange = { newMessage.value = it },
            state = state,
            modifier = Modifier.weight(.1f).padding(bottom = 8.dp),
            onSend = {
                if (newMessage.value.isNotBlank()) {
                    scope.launch {
                        viewModel.sendMessage(newMessage.value)
                        newMessage.value = ""
                    }
                }
            }
        )
    }
}

@Composable
private fun Chat(
    messages: List<AiViewModel.Message>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        reverseLayout = true
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(
            items = messages.reversed()
        ) { message ->
            MessageBubble(message)
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun MessageBubble(message: AiViewModel.Message) {
    val backgroundColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.primary else colorScheme.onBackground,
        animationSpec = tween(durationMillis = 300)
    )

    val textColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.background else colorScheme.secondary, // Colores de texto correspondientes
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(if (message.isUser) 0.8f else 1f)
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isUser) 16.dp else 0.dp,
                        topEnd = if (message.isUser) 0.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            MarkdownText(
                markdown = message.text,
                color = textColor
            )
        }
    }
}

@Composable
private fun TextFieldAi(
    value: String,
    state: AiViewModel.AiState,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(24.dp),
            maxLines = 5,
            placeholder = { Text(text = "Type your message here") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.onBackground,
                unfocusedContainerColor = colorScheme.onBackground,
                focusedIndicatorColor = colorScheme.onBackground,
                unfocusedIndicatorColor = colorScheme.onBackground,
                cursorColor = colorScheme.tertiary,
                focusedTextColor = colorScheme.tertiary,
                unfocusedTextColor = colorScheme.tertiary,
                focusedPlaceholderColor = colorScheme.primary,
                unfocusedPlaceholderColor = colorScheme.tertiary,
                focusedTrailingIconColor = colorScheme.primary,
                unfocusedTrailingIconColor = colorScheme.tertiary,
                focusedLeadingIconColor = colorScheme.primary,
                unfocusedLeadingIconColor = colorScheme.tertiary,
            ),
            trailingIcon = {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .hoverable(
                                enabled = true,
                                interactionSource = interactionSource
                            )
                            .background(colorScheme.primary, RoundedCornerShape(16.dp)),
                        onClick = { onSend() },
                        content = {
                            Crossfade(state.isLoading) { isLoading ->
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = colorScheme.onBackground
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(Res.drawable.arrow_upward_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                        contentDescription = null,
                                        tint = colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}