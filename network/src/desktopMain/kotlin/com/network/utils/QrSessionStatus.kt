package com.network.utils

// Define los posibles estados que el listener puede emitir
sealed class QrSessionStatus {
    data object Pending : QrSessionStatus() // Esperando escaneo/procesamiento backend
    data class Accepted(val desktopToken: String) : QrSessionStatus() // Backend confirmó y envió token
    data class Error(val message: String) : QrSessionStatus() // Error específico del listener o estado de error
    data object NotFoundOrExpired : QrSessionStatus() // Documento no encontrado o expirado
}