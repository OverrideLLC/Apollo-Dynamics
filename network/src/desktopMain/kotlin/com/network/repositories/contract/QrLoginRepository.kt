package com.network.repositories.contract

import com.network.utils.QrSessionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones para el flujo de login con QR.
 * Actúa como contrato entre el ViewModel y la fuente de datos (Service).
 */
interface QrLoginRepository {
    /**
     * Inicia una nueva sesión QR creando el registro inicial en Firestore.
     * @return Result que contiene el ID único de la sesión (UUID) en caso de éxito, o una excepción en caso de fallo.
     */
    suspend fun createSession(): Result<String>

    /**
     * Escucha los cambios de estado de una sesión QR específica en Firestore.
     * @param sessionId El ID de la sesión a escuchar.
     * @return Un Flow que emite los diferentes estados [QrSessionStatus] de la sesión.
     */
    fun listenToSession(sessionId: String): Flow<QrSessionStatus>

    /**
     * Intenta borrar el documento de la sesión QR en Firestore.
     * Útil si el usuario cancela el proceso.
     * @param sessionId El ID de la sesión a borrar.
     * @return Result que indica éxito (Unit) o fallo.
     */
    suspend fun cancelSession(sessionId: String): Result<Unit>

    // NOTA: La función para iniciar sesión (`signInWithCustomToken`) NO está aquí.
    // El repositorio solo provee el token a través del Flow `listenToSession`.
    // El ViewModel será responsable de llamar al SDK de CLIENTE para el login.
}
