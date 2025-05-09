package com.feature.desktop.home.services.classroom.uploadassignment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.components.ListCourses
import com.google.api.services.classroom.model.CourseWork

// Composable for the upload assignment screen
@Composable
fun UploadAssignmentScreen(
    viewModel: UploadAssignmentViewModel,
    assignmentTitle: String,
    assignmentDescription: String,
    maxPoints: Double? = 100.0,
    onUploadSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Side effect to handle the upload result
    LaunchedEffect(uiState.uploadSuccess, uiState.error) {
        if (uiState.uploadSuccess) {
            onUploadSuccess()
            viewModel.resetState() // Reset the state after success
        }
        uiState.error?.let { errorMessage ->
            onError(errorMessage)
            viewModel.resetState() // Reset the state after error
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListCourses(
            courses =
        )
        HorizontalDivider()
        Text("Upload Assignment", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Course ID: $courseId")
        Text("Title: $assignmentTitle")
        Text("Description: $assignmentDescription")
        maxPoints?.let { Text("Maximum points: $it") }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Create the CourseWork object with the passed details
                val coursework = CourseWork().apply {
                    title = assignmentTitle
                    description = assignmentDescription
                    this.maxPoints = maxPoints // Use 'this.' to avoid ambiguity
                    // Configure other CourseWork fields as needed (e.g., workType, state)
                    workType = "ASSIGNMENT" // Other options: QUESTION, MATERIAL, etc.
                    state = "PUBLISHED" // Other options: DRAFT, DELETED
                    // You can add attachments here if the assignment includes files, links, etc.
                    // See Classroom API documentation for more details on CourseWork
                }
                viewModel.uploadAssignment(courseId, coursework)
            },
            enabled = !uiState.isLoading // Disable the button while loading
        ) {
            Text("Upload Assignment to Classroom")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show loading indicator
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Text("Uploading assignment...")
        }

        // Success/error messages are handled via callbacks
    }
}
