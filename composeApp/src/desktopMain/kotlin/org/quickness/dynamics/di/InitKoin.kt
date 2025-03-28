package org.quickness.dynamics.di

import com.feature.desktop.api.di.viewModelModule
import com.network.di.repositoryModule
import com.network.di.serviceModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    printLogger()
    modules(
        viewModelModule,
        repositoryModule,
        serviceModule
    )
}