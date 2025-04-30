package com.feature.desktop.start.screen

// Re-definir QrUiState aquí por simplicidad
data class QrUiState(
    val loginState: QrLoginState = QrLoginState.IDLE,
    val qrContent: String? = null,
    val errorMessage: String? = null
)
