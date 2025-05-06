package com.network.init

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun initFirebase(): FirebaseApp? {
    try {
        val serviceAccount = FileInputStream("/home/christopher-cop787-gmail-com/Documentos/Credentials/Firebase/quickness-backend-f.json") // ¡Cuidado con la ruta!
        val credentials = GoogleCredentials.fromStream(serviceAccount)
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .setProjectId("quickness-backend-7f4ac")
            .build()
        FirebaseApp
            .initializeApp(options)
        return FirebaseApp.getInstance()
    } catch (e: Exception) {
        println("Error inicializando Firebase: ${e.message}")
    }
    return null
}