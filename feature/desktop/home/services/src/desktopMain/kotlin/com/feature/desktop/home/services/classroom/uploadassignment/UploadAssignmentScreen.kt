package com.feature.desktop.home.services.classroom.uploadassignment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.components.ListCourses
import com.google.api.services.classroom.model.CourseWork
import org.koin.compose.viewmodel.koinViewModel

// Composable for the upload assignment screen
@Composable
fun UploadAssignmentScreen(
    viewModel: UploadAssignmentViewModel = koinViewModel(),
    initialAssignmentTitle: String,
    initialAssignmentDescription: String,
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

    var assignmentTitle by remember { mutableStateOf(initialAssignmentTitle) }
    var assignmentDescription by remember { mutableStateOf(initialAssignmentDescription) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListCourses(
            courses = uiState.courses ?: emptyList(),
            selectedCourseId = uiState.selectedCourse?.id ?: "",
            onCourseClick = { courseId ->
                viewModel.selectCourse(courseId)
            }
        )
        HorizontalDivider()
        Text("Upload Assignment", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(24.dp))

        uiState.selectedCourse?.let { course ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize() // Use fillMaxSize for the column to take available space
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
            ){
                item {
                    Text("Upload Assignment Details", style = MaterialTheme.typography.h5)
                }

                item {
                    // TextField for Assignment Title
                    // TextField para el Título de la Tarea
                    OutlinedTextField( // Using OutlinedTextField for better visual separation
                        value = assignmentTitle,
                        onValueChange = { assignmentTitle = it },
                        label = { Text("Assignment Title") },
                        modifier = Modifier.fillMaxWidth() // Make it fill the width
                    )
                }

                item {
                    // TextField for Assignment Description
                    // TextField para la Descripción de la Tarea
                    OutlinedTextField( // Using OutlinedTextField
                        value = assignmentDescription,
                        onValueChange = { assignmentDescription = it },
                        label = { Text("Assignment Description") },
                        modifier = Modifier
                            .fillMaxWidth() // Fill width
                            .height(200.dp) // Give it a fixed height or use weight for flexibility
                    )
                }

                maxPoints?.let {
                    item {
                        Text("Maximum points: $it")
                        // If maxPoints were editable, you'd add a TextField here too
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Add some space before the button

                    Button(
                        onClick = {
                            // Create the CourseWork object with the current values from TextFields
                            // Crea el objeto CourseWork con los valores actuales de los TextFields
                            val coursework = CourseWork().apply {
                                title = assignmentTitle // Use the state variable
                                description = assignmentDescription // Use the state variable
                                this.maxPoints = maxPoints // Use the passed parameter (or editable state if applicable)
                                // Configure other CourseWork fields as needed (ej. workType, state)
                                workType = "ASSIGNMENT" // Other options: QUESTION, MATERIAL, etc.
                                state = "PUBLISHED" // Other options: DRAFT, DELETED
                                // You can add attachments here if the assignment includes files, links, etc.
                                // See Classroom API documentation for more details on CourseWork
                            }
                            // Assuming course.id is available from a different state or passed parameter
                            // If course.id comes from uiState.selectedCourse, ensure that state is handled.
                            // For now, using the passed courseId parameter.
                            viewModel.uploadAssignment(course.id, coursework)
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
        }
    }
}
