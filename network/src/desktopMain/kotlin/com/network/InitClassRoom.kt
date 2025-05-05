package com.network

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.ClassroomScopes
import com.network.credentials.ClassroomCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.io.IOException

fun initializeClassroomClient(coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        try {
            val service = withContext(Dispatchers.Swing) {
                val transport = NetHttpTransport()
                val jsonFactory = GsonFactory.getDefaultInstance()

                val scopes = listOf(
                    ClassroomScopes.CLASSROOM_COURSES_READONLY,
                    ClassroomScopes.CLASSROOM_ANNOUNCEMENTS_READONLY
                )

                val credential = ClassroomCredentials.getCredentials(transport, scopes)

                Classroom.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Apollo")
                    .build()
            }

        } catch (e: IOException) {
            println("Error initializing Classroom service: ${e.message}")
            e.printStackTrace()
        } catch (t: Throwable) {
            println("Unexpected error initializing Classroom service: ${t.message}")
            t.printStackTrace()
        } finally {

        }
    }
}