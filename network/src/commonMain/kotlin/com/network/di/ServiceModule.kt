package com.network.di

import com.network.interfaces.GeminiService
import com.network.service.GeminiServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serviceModule: Module
    get() = module {
        singleOf(::GeminiServiceImpl).bind(GeminiService::class)
    }