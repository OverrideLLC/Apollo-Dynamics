package org.quickness.dynamics.di

import com.feature.desktop.api.di.viewModelModule
import com.network.di.classroomModule
import com.network.di.firebaseModule
import com.network.di.repositoryModule
import com.network.di.serviceModule
import com.override.data.di.daoModule
import com.override.data.di.dataModule
import com.override.data.di.repositoryDataModule
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
        MainModule,
        dataModule,
        daoModule,
        repositoryDataModule,
        firebaseModule,
        classroomModule
    )
}