package com.network.di

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.network.repositories.contract.ClassroomRepository
import com.network.repositories.impl.ClassroomRepositoryImpl
import com.network.services.ClassroomServices
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val classroomModule = module {
    single<GsonFactory> { GsonFactory.getDefaultInstance() }
    single<NetHttpTransport> { NetHttpTransport() }
    factoryOf(::ClassroomServices)
    factoryOf(::ClassroomRepositoryImpl).bind(ClassroomRepository::class)
}