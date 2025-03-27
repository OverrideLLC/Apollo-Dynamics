package org.quickness.dynamics.di

import com.feature.desktop.api.di.viewModelModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    printLogger()
    modules(
        viewModelModule
    )
}