package com.feature.desktop.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun Content(
    padding: PaddingValues,
    workspace: @Composable () -> Unit
) {
    Column( // Cambia LazyColumn a Column
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Task(modifier = Modifier.weight(0.3f).fillMaxHeight())
            WorkSpace(modifier = Modifier.weight(0.4f).fillMaxHeight(), workspace = workspace)
            Tools(modifier = Modifier.weight(0.3f).fillMaxHeight())
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
internal fun WorkSpace(
    workspace: @Composable () -> Unit,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            ),
        content = { workspace() }
    )
}

@Composable
internal fun Task(
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            ),
        content = {
            Text(text = "Task", color = colorScheme.primary, fontSize = 20.sp)
        }
    )
}

@Composable
internal fun Tools(
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            ),
        content = {
            Text(text = "Tools", color = colorScheme.primary, fontSize = 20.sp)
        }
    )
}
