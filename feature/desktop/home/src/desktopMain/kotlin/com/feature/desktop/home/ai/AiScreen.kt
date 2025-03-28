package com.feature.desktop.home.ai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 70.dp,
                bottom = 157.dp,
                start = 100.dp,
                end = 100.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Chat(messages)
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        TextFieldAi(
            value = newMessage.value,
            onValueChange = { newMessage.value = it },
            state = state,
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
    messages: List<AiViewModel.Message>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = messages
        ) { message ->
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = remember {
                        tween(durationMillis = 500)
                    }
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
                modifier = Modifier.animateItem()
            ) {
                MessageBubble(message)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: AiViewModel.Message) {
    val backgroundColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.onBackground else colorScheme.secondary,
        animationSpec = tween(durationMillis = 300)
    )

    val textColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.tertiary else colorScheme.onSecondary,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .pointerInput(Unit) {
                detectTransformGestures { _, _, _, _ ->
                    // Disable pointer input for bubble
                }
            },
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        AnimatedContent(
            targetState = message,
            transitionSpec = {
                (fadeIn() + expandVertically()).togetherWith(fadeOut() + shrinkVertically())
            }
        ) { targetMessage ->
            Text(
                text = targetMessage.text,
                color = textColor,
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .padding(12.dp)
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun TextFieldAi(
    value: String,
    state: AiViewModel.AiState,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            maxLines = 7,
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