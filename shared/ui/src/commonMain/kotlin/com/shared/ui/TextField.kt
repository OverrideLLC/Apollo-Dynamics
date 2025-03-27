package com.shared.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldCustom(
    value: String,
    onDone: () -> Unit = {},
    onGo: () -> Unit = {},
    onSearch: () -> Unit = {},
    onSend: () -> Unit = {},
    keyboardType: KeyboardType,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.tertiary,
            cursorColor = colorScheme.tertiary,
            focusedTextColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.tertiary,
            focusedPlaceholderColor = colorScheme.primary,
            unfocusedPlaceholderColor = colorScheme.tertiary,
        ),
        shape = RoundedCornerShape(32.dp),
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
            onGo = { onGo() },
            onSearch = { onSearch() },
            onSend = { onSend() }
        ),
        modifier = Modifier.size(300.dp, 50.dp)
    )
}