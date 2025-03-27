package com.feature.desktop.home.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shared.ui.TextFieldCustom
import kotlinx.coroutines.launch
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 128.dp, top = 128.dp)
            .background(Color.Transparent),
    ) {
        Chat(
            messages = messages,
            modifier = Modifier.weight(1f)
        )
        TextFieldAi(
            value = newMessage.value,
            onValueChange = { newMessage.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 128.dp, end = 128.dp),
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(messages.size) { index ->
            val message = messages[index]
            MessageBubble(message = message)
        }
    }
}

@Composable
private fun MessageBubble(message: AiViewModel.Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.text,
            color = if (message.isUser) colorScheme.onPrimary else colorScheme.onSecondary,
            modifier = Modifier
                .background(
                    color = if (message.isUser) colorScheme.primary else colorScheme.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        )
    }
}

@Composable
private fun TextFieldAi(
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextFieldCustom(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Ask me anything",
            keyboardType = KeyboardType.Text,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onSend) {
            Text("Enviar")
        }
    }
}
