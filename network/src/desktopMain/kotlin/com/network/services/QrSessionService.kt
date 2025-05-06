package com.network.services

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutures
import com.google.cloud.Timestamp
import com.google.cloud.firestore.Firestore
import com.network.utils.status.QrSessionStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class QrSessionService(
    private val firestore: Firestore, // Instancia de Cloud Client Library o Admin SDK
) {
    // Crea la sesión inicial en Firestore
    fun createQrSession(): Result<String> = runCatching {
        val sessionId = UUID.randomUUID().toString()
        val initialData = mapOf(
            "status" to "PENDING",
            "createdAt" to Timestamp.now() // Usar Cloud Timestamp
        )
        // Usar .get() para esperar síncronamente el resultado de ApiFuture
        // Esto bloqueará el hilo de la corrutina actual. Asegúrate que se llame desde un dispatcher apropiado (IO/Default).
        firestore.collection("AuthQr")
            .document(sessionId)
            .set(initialData)
            .get()
        sessionId
    }

    // Escucha cambios en una sesión específica y emite estados vía Flow
    fun listenToQrSession(sessionId: String): Flow<QrSessionStatus> = callbackFlow {
        println("Service: Iniciando listener para $sessionId")
        val docRef = firestore.collection("AuthQr").document(sessionId)
        var tokenAlreadyProcessed = false

        val listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            // La lógica interna del listener es mayormente la misma,
            // ya que recibe el snapshot y error de forma similar.
            if (tokenAlreadyProcessed) {
                println("Service Listener: Estado ya procesado para $sessionId, ignorando.")
                return@addSnapshotListener
            }
            // ... (resto de la lógica del when(status) como estaba antes) ...

            if (error != null) {
                println("Service Listener: Error - ${error.message}")
                // Intenta extraer un código de error si es posible (puede variar)
                val errorCode = error.code
                trySend(QrSessionStatus.Error("Error de conexión: $errorCode"))
                tokenAlreadyProcessed = true
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val status = snapshot.getString("status")
                val desktopToken = snapshot.getString("jwt") // Tu campo de token

                println("Service Listener: Recibido status=$status, hasToken=${desktopToken != null}") // Debug

                when (status) {
                    "PENDING" -> {
                        trySend(QrSessionStatus.Pending)
                    }

                    "ACCEPTED" -> {
                        if (desktopToken != null) {
                            println("Service Listener: Estado ACCEPTED con token.")
                            trySend(QrSessionStatus.Accepted(desktopToken)) // Solo envía el token
                            tokenAlreadyProcessed = true
                            close() // Cierra el Flow
                        } else {
                            println("Service Listener: Error - Estado ACCEPTED pero sin token JWT.")
                            trySend(QrSessionStatus.Error("Error inesperado: Falta token en estado aceptado."))
                            tokenAlreadyProcessed = true
                            close(IllegalStateException("Missing token in ACCEPTED state"))
                        }
                    }

                    else -> {
                        println("Service Listener: Estado final no reconocido o de error: $status")
                        trySend(QrSessionStatus.Error("Sesión finalizada con estado: $status"))
                        tokenAlreadyProcessed = true
                        close(IllegalStateException("Unhandled status: $status"))
                    }
                }
            } else {
                println("Service Listener: Documento $sessionId no encontrado.")
                trySend(QrSessionStatus.NotFoundOrExpired)
                tokenAlreadyProcessed = true
                close(IllegalStateException("Document $sessionId not found"))
            }
        }

        awaitClose {
            println("Service: Deteniendo listener para $sessionId")
            listenerRegistration.remove()
        }
    }

    // --- ELIMINADO ---
    // La función signInWithCustomToken NO pertenece aquí si usas Admin SDK/Cloud Client.
    // El inicio de sesión real debe hacerse en el ViewModel/UI con el SDK de CLIENTE.
    /*
    suspend fun signInWithCustomToken(token: String): Result<FirebaseUser> = runCatching {
        // ... Código incorrecto eliminado ...
    }
    */
    // --- FIN ELIMINADO ---

    // Borra la sesión de Firestore
    suspend fun deleteQrSession(sessionId: String): Result<Unit> = runCatching {
        // Usar .get() para esperar síncronamente el resultado de ApiFuture
        firestore.collection("AuthQr").document(sessionId)
            .delete()
            .get() // Esperar a que ApiFuture<WriteResult> complete
        // No devuelve nada en éxito
    }

    // --- Helper opcional para convertir ApiFuture a suspend (alternativa a .get()) ---
    // Puedes usar esto en lugar de .get() si prefieres un enfoque más puramente suspend
    suspend fun <T> ApiFuture<T>.awaitSuspend(): T = suspendCoroutine { continuation ->
        ApiFutures.addCallback(
            this,
            object : com.google.api.core.ApiFutureCallback<T> {
                override fun onSuccess(result: T?) {
                    // El resultado puede ser nulo para operaciones como delete/set sin retorno específico
                    continuation.resume(result as T) // Cuidado con el cast si T puede ser Unit
                }

                override fun onFailure(t: Throwable) {
                    continuation.resumeWithException(t)
                }
            },
            com.google.common.util.concurrent.MoreExecutors.directExecutor()
        ) // Ejecutar callback directamente
    }
    // Ejemplo de uso: firestore.collection(...).delete().awaitSuspend()

}