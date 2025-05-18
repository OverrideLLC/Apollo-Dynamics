package com.feature.desktop.home.task

import androidx.compose.runtime.Immutable
import com.feature.desktop.home.task.utils.TaskData

@Immutable
data class TaskState(
    val isLoading: Boolean = false,
    val tasks: List<TaskData> = listOf( // Example list, replace with actual data source (Lista de ejemplos, reemplace con la fuente de datos real)
        TaskData(title = "Grade Papers", description = "Math, Science, History"), // Calificar exámenes: Matemáticas, Ciencias, Historia
        TaskData(title = "Prepare Lesson Plans", description = "Review curriculum and create activities"), // Preparar planes de lecciones: Revisar el plan de estudios y crear actividades
        TaskData(title = "Attend Staff Meeting", description = "Discuss school updates and policies") // Asistir a la reunión de personal: Discutir actualizaciones y políticas escolares
    ),
    val error: String? = null,
)
