package com.feature.desktop.home.services.classroom.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.services.announcement.ClassroomAnnouncementState
import com.feature.desktop.home.services.classroom.services.announcement.ClassroomAnnouncementViewModel

@Composable
internal fun AnnouncementCreator(
    state: ClassroomAnnouncementState,
    viewModel: ClassroomAnnouncementViewModel,
    announcement: String,
) {
    var currentAnnouncementText by remember(announcement) { mutableStateOf(announcement) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Create Announcement and Actions",
            style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = currentAnnouncementText,
                onValueChange = {
                    currentAnnouncementText = it
                },
                label = {
                    Text(
                        text = "Write your announcement...",
                        style = typography.bodySmall,
                        color = colorScheme.onSurface
                    )
                },
                modifier = Modifier
                    .weight(.5f)
                    .height(150.dp),
                maxLines = 6,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true
                ),
                shape = shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = colorScheme.surface, // Un fondo sutil
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline,
                    textColor = colorScheme.onSurface,
                    cursorColor = colorScheme.primary
                )
            )

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxWidth(.5f)
            ) {
                Announcement(
                    state = state,
                    viewModel = viewModel,
                    announcement = currentAnnouncementText
                )
            }
        }
    }
}
