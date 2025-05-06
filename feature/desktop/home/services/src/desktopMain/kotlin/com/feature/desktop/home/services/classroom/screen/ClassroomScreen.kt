package com.feature.desktop.home.services.classroom.screen

// import androidx.compose.foundation.layout.wrapContentSize // Not used directly
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.services.classroom.utils.enums.ClassroomServices
import com.google.api.services.classroom.model.Course
import com.shared.ui.ClassWidget
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClassroomScreen() = Screen()

@Composable
internal fun Screen(
    viewModel: ClassroomViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState() // Use 'by' delegate for cleaner state access
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.surface),
        contentAlignment = Alignment.TopCenter, // Align content to the top
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListCourses(
                state = state,
                onCourseClick = { course -> // Pass the full course object
                    viewModel.onCourseSelected(course)
                }
            )
            // Conditionally display ListServices based on the state
            if (state.servicesVisible) {
                Spacer(modifier = Modifier.size(24.dp)) // Add some space before services
                ListServices(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
internal fun ListServices(
    state: ClassroomState,
    viewModel: ClassroomViewModel
) {
    val scrollStateServices = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 16.dp), // Padding for the row items
            state = scrollStateServices
        ) {
            items(items = state.classroomServices) { service ->
                CardService(
                    icon = service.icon,
                    title = service.title,
                    onClick = {
                        // Ensure a course is selected before performing actions that require a courseId
                        state.selectedCourseId?.let { courseId ->
                            when (service) {
                                ClassroomServices.ADD_ANNOUNCE -> {
                                    // You might want to show a dialog here to get announcement content
                                    viewModel.addAnnouncement(
                                        "Nuevo Anuncio desde Jetpack Compose!",
                                        courseId
                                    )
                                }
                                // TODO: Handle other classroom services based on 'service'
                            }
                        }
                    },
                    size = 180.dp // Slightly smaller cards for services
                )
            }
        }
        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Match padding of LazyRow
                .padding(top = 8.dp), // Space between cards and scrollbar
            adapter = rememberScrollbarAdapter(
                scrollState = scrollStateServices
            )
        )
    }
}


@Composable
fun ListCourses(
    state: ClassroomState,
    onCourseClick: (Course) -> Unit // Callback with the Course object
) {
    val scrollStateCourses = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp), // Add some top padding
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            ), // Spacing between course cards
            contentPadding = PaddingValues(horizontal = 16.dp), // Padding for the row items
            state = scrollStateCourses
        ) {
            state.courses?.let { courses ->
                items(items = courses) { course ->
                    ClassWidget(
                        name = course.name ?: "Clase sin nombre",
                        color = colorScheme.onPrimaryContainer,
                        career = course.descriptionHeading ?: "", // Example: using descriptionHeading for career
                        degree = "", // Placeholder or map from course data
                        section = course.section ?: "General",
                        onClick = { onCourseClick(course) }, // Invoke callback with the course
                        delete = {},
                        isSelected = course.id == state.selectedCourseId,
                        isEnabledDeleted = false
                    )
                }
            }
        }
        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Match padding of LazyRow
                .padding(top = 8.dp), // Space between cards and scrollbar
            adapter = rememberScrollbarAdapter(
                scrollState = scrollStateCourses
            )
        )
    }
}

@Composable
internal fun CardService(
    size: Dp = 100.dp,
    icon: DrawableResource,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(size),
        shape = shapes.medium, // Use medium shapes for a softer look
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceContainer // Use a distinct color for service cards
        ),
        onClick = onClick,
        content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp), // Adjusted padding
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title, // Good for accessibility
                    modifier = Modifier.size(size / 3.5f), // Adjusted icon size
                    tint = colorScheme.primary // Ensure contrast
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium, // Adjusted style for card titles
                    color = colorScheme.primary, // Ensure contrast
                    // fontSize = 24.sp // Overriding fontSize here, consider defining in typography
                )
            }
        }
    )
}
