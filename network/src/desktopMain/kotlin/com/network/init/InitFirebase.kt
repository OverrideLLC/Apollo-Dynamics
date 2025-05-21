package com.network.init

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun initFirebase(): FirebaseApp? {
    try {

        return FirebaseApp.getInstance()
    } catch (e: Exception) {
        println("Error inicializando Firebase: ${e.message}")
    }
    return null
}