package com.network.di

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.network.services.ClassroomServices
import org.koin.dsl.module

val classroomModule = module {
    single<GsonFactory> { GsonFactory.getDefaultInstance() }
    single<NetHttpTransport> { NetHttpTransport() }
    single {
        ClassroomServices(get(), get())
    }
}