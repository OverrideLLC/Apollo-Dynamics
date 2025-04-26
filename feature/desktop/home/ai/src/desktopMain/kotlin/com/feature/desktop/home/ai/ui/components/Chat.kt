package com.feature.desktop.home.ai.ui.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.shared.resources.Res
import com.shared.resources.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun Chat(
    messages: List<AiViewModel.Message>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    viewModel: AiViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.scrollToItem(index = messages.size)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(
                12.dp,
                Alignment.Bottom
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(
                items = messages,
                key = { message -> message.id }
            ) { message ->
                SelectionContainer {
                    MessageBubble(message, viewModel)
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = listState)
        )

        IconButton(
            onClick = { viewModel.clearChat() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
                .padding(horizontal = 8.dp)
                .size(24.dp)
                .background(
                    color = colorScheme.onTertiary,
                    shape = shapes.small
                ),
            content = {
                Icon(
                    painter = painterResource(Res.drawable.delete_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = "Eliminar chat",
                    modifier = Modifier.fillMaxSize(),
                    tint = colorScheme.primary
                )
            }
        )
    }
}