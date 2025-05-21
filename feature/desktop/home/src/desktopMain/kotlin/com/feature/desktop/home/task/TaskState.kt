package com.feature.desktop.home.task

import androidx.compose.runtime.Immutable
import com.feature.desktop.home.task.utils.TaskData

@Immutable
data class TaskState(
    val estaCargando: Boolean = false,
    val tasks: List<TaskData> = listOf( // Lista de ejemplos, reemplace con la fuente de datos real
        TaskData(title = "Calificar exámenes", description = "Matemáticas, Ciencias, Historia"),
        TaskData(title = "Preparar planes de lecciones", description = "Revisar el plan de estudios y crear actividades"),
        TaskData(title = "Asistir a la reunión de personal", description = "Discutir actualizaciones y políticas escolares")
    ),
    val error: String? = null,
)
