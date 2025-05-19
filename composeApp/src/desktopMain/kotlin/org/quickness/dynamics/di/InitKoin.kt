package org.quickness.dynamics.di

import com.feature.desktop.api.di.viewModelModule
import com.network.di.classroomModule
import com.network.di.firebaseModule
import com.network.di.repositoryModule
import com.network.di.serviceModule
import com.network.init.initFirebase
import com.override.data.di.daoModule
import com.override.data.di.dataModule
import com.override.data.di.repositoryDataModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.koin.core.context.startKoin
import org.koin.dsl.module

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
        classroomModule,
    )
}