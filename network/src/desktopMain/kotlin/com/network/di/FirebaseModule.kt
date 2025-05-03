package com.network.di

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import com.network.initFirebase
import com.network.repositories.contract.QrLoginRepository
import com.network.repositories.impl.QrLoginRepositoryImpl
import com.network.services.QrSessionService
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val firebaseModule: Module = module {
    factoryOf(::QrLoginRepositoryImpl).bind(QrLoginRepository::class)
    factoryOf(::QrSessionService)
    single<Firestore> {
        FirestoreClient.getFirestore(get<FirebaseApp>())
    }
    single<FirebaseApp> {
        initFirebase() ?: throw IllegalStateException("Firebase no inicializado")
    }
}