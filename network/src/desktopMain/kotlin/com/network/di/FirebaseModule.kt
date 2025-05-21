package com.network.di

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import com.network.init.initFirebase
import com.network.repositories.contract.QrLoginRepository
import com.network.repositories.impl.QrLoginRepositoryImpl
import com.network.services.QrSessionService
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule: Module = module {
    single<FirebaseApp> {
        initFirebase() ?: throw IllegalStateException("Firebase no inicializado")
    }
    factoryOf(::QrLoginRepositoryImpl).bind(QrLoginRepository::class)
    factoryOf(::QrSessionService)
    single<Firestore> {
        FirestoreClient.getFirestore(get<FirebaseApp>())
    }
}