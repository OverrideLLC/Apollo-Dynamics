package com.feature.desktop.home.services.classroom.services.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.network.repositories.contract.ClassroomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewmodel(
    private val repositoryClassroom: ClassroomRepository
): ViewModel() {
    data class ReportState(
        val isLoading: Boolean = false,
        val report: String? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(ReportState())
    val state = _state.asStateFlow()

    fun generateReport(
        classId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
             repositoryClassroom.getCourseStudents(classId)
            }.onSuccess { students ->
                students.forEach { student ->
                    student.studentWorkFolder
                }
            }.onFailure {

            }
        }
    }
}