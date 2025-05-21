package org.quickness.dynamics.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.quickness.dynamics.ApplicationViewModel

val MainModule: Module
    get() = module {
        viewModelOf(::ApplicationViewModel)
    }