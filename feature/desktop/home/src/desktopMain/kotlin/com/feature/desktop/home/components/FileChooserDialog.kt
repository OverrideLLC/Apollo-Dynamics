package com.feature.desktop.home.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JOptionPane

private const val MAX_FILES_LIMIT = 4

fun fileChooserDialog(
    parent: Frame? = null,
    onResult: (result: List<File>) -> Unit
) {
    val dialog = FileDialog(parent, "Select Files", FileDialog.LOAD).apply {
        isMultipleMode = true
        isVisible = true
    }
    val allSelectedFiles = dialog.files?.toList() ?: emptyList()

    val limitedFiles = if (allSelectedFiles.size > MAX_FILES_LIMIT) {
        JOptionPane.showMessageDialog(
            parent,
            "You selected ${allSelectedFiles.size} files. Only the first $MAX_FILES_LIMIT were kept.",
            "File Limit Exceeded",
            JOptionPane.WARNING_MESSAGE
        )
        allSelectedFiles.take(MAX_FILES_LIMIT)
    } else {
        allSelectedFiles
    }
    onResult(limitedFiles)
}

@Composable
fun SelectedFilesPreview(
    files: List<File>,
    onRemoveFile: (File) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    if (files.isNotEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
        ){
            LazyRow(
                state = listState,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    Row(
                        modifier = Modifier
                            .background(
                                color = colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .width(200.dp)
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = file.name,
                            color = colorScheme.tertiary,
                            maxLines = 1,
                            modifier = Modifier.weight(
                                1f,
                                fill = false
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        IconButton(
                            onClick = { onRemoveFile(file) },
                            modifier = Modifier.size(18.dp) // Icono pequeño
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar archivo ${file.name}",
                                tint = colorScheme.tertiary
                            )
                        }
                    }
                }
            }
            HorizontalScrollbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                adapter = rememberScrollbarAdapter(scrollState = listState),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = colorScheme.primary,
                    unhoverColor = colorScheme.tertiary
                )
            )
        }
    }
}