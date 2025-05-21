package com.feature.desktop.home.ai.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.feature.desktop.home.ai.ui.components.AnimationEva
import com.feature.desktop.home.ai.ui.components.Content
import com.feature.desktop.home.ai.ui.components.Screens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AiScreen() = Screen()

@Composable
internal fun Screen(viewModel: AiViewModel = koinViewModel()) {
    LaunchedEffect(Unit) { viewModel.loadData() }
    val state by viewModel.state.collectAsState()
    val initiallyVisible = state.messages.isNotEmpty()
    var showFileChooser by remember { mutableStateOf(false) }
    val text = "Hola, ¿en qué puedo ayudarte?"
    var displayedText by remember(
        text,
        initiallyVisible
    ) { mutableStateOf(if (initiallyVisible) text else "") }
    val alpha = remember(text, initiallyVisible) { Animatable(if (initiallyVisible) 1f else 0f) }

    AnimationEva(
        text = text,
        initiallyVisible = initiallyVisible,
        alpha = alpha,
        onDisplayedTextChange = { displayedText = it }
    )

    Content(
        state = state,
        displayedText = displayedText,
        alpha = alpha,
        showFileChooser = { showFileChooser = it }
    )

    Screens(
        viewModel = viewModel,
        state = state,
        showFileChooserState = showFileChooser,
        showFileChooser = { showFileChooser = it }
    )
}