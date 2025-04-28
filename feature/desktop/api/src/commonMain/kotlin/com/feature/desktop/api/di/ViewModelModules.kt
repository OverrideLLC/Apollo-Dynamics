package com.feature.desktop.api.di

import com.feature.desktop.home.HomeViewModel
import com.feature.desktop.home.ai.ui.screen.AiViewModel
import com.feature.desktop.home.tools.ToolViewModel
import com.feature.desktop.home.tools.screens.add_class.AddClassViewModel
import com.feature.desktop.home.tools.screens.take_attendees.TakeAttendeesViewModel
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
    }