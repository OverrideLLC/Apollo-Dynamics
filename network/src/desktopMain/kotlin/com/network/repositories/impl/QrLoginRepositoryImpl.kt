package com.network.repositories.impl// Asegúrate de importar las clases/interfaces necesarias

import com.network.repositories.contract.QrLoginRepository
import com.network.services.QrSessionService
import com.network.utils.status.QrSessionStatus
import kotlinx.coroutines.flow.Flow


/**
 * Implementación concreta de [QrLoginRepository].
 * Delega las llamadas al [QrSessionService].
 */
class QrLoginRepositoryImpl(
    private val qrSessionService: QrSessionService // Inyectar la dependencia del servicio
) : QrLoginRepository {

    /**
     * Llama al servicio para crear la sesión QR.
     */
    override suspend fun createSession(): Result<String> {
        // Simplemente delega la llamada al servicio
        return qrSessionService.createQrSession()
    }

    /**
     * Llama al servicio para obtener el Flow que escucha la sesión QR.
     */
    override fun listenToSession(sessionId: String): Flow<QrSessionStatus> {
        // Delega la llamada al servicio
        return qrSessionService.listenToQrSession(sessionId)
    }

    /**
     * Llama al servicio para borrar la sesión QR.
     */
    override suspend fun cancelSession(sessionId: String): Result<Unit> {
        // Delega la llamada al servicio
        return qrSessionService.deleteQrSession(sessionId)
    }
}