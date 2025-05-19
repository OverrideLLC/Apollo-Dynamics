package com.network.init

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun initFirebase(): FirebaseApp? {
    try {
        val serviceAccountStream = object {}.javaClass.getResourceAsStream("/firebase.json")
        val credentials = GoogleCredentials.fromStream(serviceAccountStream)
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