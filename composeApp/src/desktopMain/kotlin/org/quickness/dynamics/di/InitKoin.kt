package org.quickness.dynamics.di

import com.feature.desktop.api.di.viewModelModule
import com.network.di.repositoryModule
import com.network.di.serviceModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.quickness.dynamics.ApplicationViewModel

fun initKoin() = startKoin {
    printLogger()
    modules(
        viewModelModule,
        repositoryModule,
        serviceModule,
        MainModule
    )
}