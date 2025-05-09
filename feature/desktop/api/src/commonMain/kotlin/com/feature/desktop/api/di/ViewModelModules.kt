package com.feature.desktop.api.di

import com.feature.desktop.home.HomeViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.services.classroom.ClassroomAnnouncementViewModel
import com.feature.desktop.home.services.classroom.services.report.ReportViewmodel
import com.feature.desktop.home.services.classroom.uploadassignment.UploadAssignmentViewModel
import com.feature.desktop.home.tools.ui.ToolViewModel
import com.feature.desktop.home.tools.ui.screens.add_class.AddClassViewModel
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentViewModel
import com.feature.desktop.home.tools.ui.screens.student_status.StudentStatusViewModel
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesViewModel
import com.feature.desktop.start.screen.StartViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule: Module
    get() = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::AiViewModel)
        viewModelOf(::ToolViewModel)
        viewModelOf(::TakeAttendeesViewModel)
        viewModelOf(::AddClassViewModel)
        viewModelOf(::StartViewModel)
        viewModelOf(::AddStudentViewModel)
        viewModelOf(::StudentStatusViewModel)
        viewModelOf(::ClassroomAnnouncementViewModel)
        viewModelOf(::ReportViewmodel)
        viewModelOf(::UploadAssignmentViewModel)
    }