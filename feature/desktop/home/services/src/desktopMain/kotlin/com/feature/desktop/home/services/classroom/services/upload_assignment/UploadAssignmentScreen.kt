package com.feature.desktop.home.services.classroom.services.upload_assignment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feature.desktop.home.services.classroom.components.ListCourses
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.CourseWork
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UploadAssignmentScreen(
    initialAssignmentTitle: String,
    initialAssignmentDescription: String,
    maxPoints: Double? = 100.0,
    onUploadSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val viewModel: UploadAssignmentViewModel? = koinViewModel()
    val uiState by viewModel!!.uiState.collectAsState()

    LaunchedEffect(uiState.uploadSuccess, uiState.error) {
        if (uiState.uploadSuccess) {
            onUploadSuccess()
            viewModel.resetState()
        }
        uiState.error?.let { errorMessage ->
            onError(errorMessage)
            viewModel.resetState()
        }
    }
    Content(
        initialAssignmentTitle = initialAssignmentTitle,
        initialAssignmentDescription = initialAssignmentDescription,
        maxPoints = maxPoints,
        uiState = uiState,
        onCourseClick = { course -> viewModel.selectCourse(course) },
        onUploadSuccess = { course, coursework ->
            viewModel.uploadAssignment(
                course.id,
                coursework
            )
        }
    )

}

@Composable
private fun Content(
    initialAssignmentTitle: String,
    initialAssignmentDescription: String,
    maxPoints: Double?,
    uiState: UploadAssignmentUiState,
    onCourseClick: (Course) -> Unit,
    onUploadSuccess: (course: Course, coursework: CourseWork) -> Unit,
) {
    var assignmentTitle by remember { mutableStateOf(initialAssignmentTitle) }
    var assignmentDescription by remember { mutableStateOf(initialAssignmentDescription) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ListCourses(
            courses = uiState.courses ?: emptyList(),
            selectedCourseId = uiState.selectedCourse?.id ?: "",
            onCourseClick = { course -> onCourseClick(course) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            color = colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(24.dp))

        uiState.selectedCourse?.let { course ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = assignmentTitle,
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        ),
                        onValueChange = { assignmentTitle = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onSurface,
                            cursorColor = colorScheme.primary
                        ),
                        label = { Text("Assignment Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = assignmentDescription,
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        ),
                        onValueChange = { assignmentDescription = it },
                        label = { Text("Assignment Description") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onSurface,
                            cursorColor = colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.primary
                        ),
                        onClick = {
                            val coursework = CourseWork().apply {
                                title = assignmentTitle
                                description = assignmentDescription
                                this.maxPoints = maxPoints
                                workType = "ASSIGNMENT"
                                state = "PUBLISHED"
                            }
                            onUploadSuccess(course, coursework)
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Upload Assignment to Classroom")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                        Text("Uploading assignment...")
                    }
                }
            }
        }
    }
}