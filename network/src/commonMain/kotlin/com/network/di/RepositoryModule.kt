package com.network.di

import com.network.interfaces.FirebaseGitliveRepository
import com.network.interfaces.GeminiRepository
import com.network.repository.FirebaseGitliveRepositoryImpl
import com.network.repository.GeminiRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule: Module
    get() = module {
        singleOf(::GeminiRepositoryImpl).bind(GeminiRepository::class)
        singleOf(::FirebaseGitliveRepositoryImpl).bind(FirebaseGitliveRepository::class)
    }