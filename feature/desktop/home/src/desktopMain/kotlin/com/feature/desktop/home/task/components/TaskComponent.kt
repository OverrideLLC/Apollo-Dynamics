package com.feature.desktop.home.task.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun TaskComponent(
    title: String,
    description: String,
    isExpanded: Boolean
) {
    val animatedSize = animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 100.dp,
        label = "animatedSize"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedSize.value),
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.onSurface,
            contentColor = colorScheme.surface
        ),
        shape = shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                content = {
                    Text(
                        text = title,
                        style = typography.titleLarge,
                        fontSize = 20.sp,
                    )
                    if (isExpanded) {
                        Text(
                            text = description,
                            style = typography.bodyMedium,
                            fontSize = 14.sp,
                        )
                    }
                }
            )
        }
    )
}