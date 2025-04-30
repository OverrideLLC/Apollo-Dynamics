package com.network

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun initFirebase(): FirebaseApp? {
    try {
        val serviceAccount = FileInputStream("C:\\Users\\chris\\Desktop\\quickness-backend-7f4ac-firebase-adminsdk-szh6q-0978bc4d55.json") // ¡Cuidado con la ruta!
        val credentials = GoogleCredentials.fromStream(serviceAccount)
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .setProjectId("quickness-backend-7f4ac")
            .build()
        FirebaseApp
            .initializeApp(options)
        return FirebaseApp.getInstance()
        println("Firebase Inicializado Correctamente.")
    } catch (e: Exception) {
        println("Error inicializando Firebase: ${e.message}")
    }
    return null
}