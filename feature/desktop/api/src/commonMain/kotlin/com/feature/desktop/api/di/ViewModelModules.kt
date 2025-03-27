package com.feature.desktop.api.di

import com.feature.desktop.home.HomeViewModel
import com.feature.desktop.home.ai.AiViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule: Module
    get() = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::AiViewModel)
    }