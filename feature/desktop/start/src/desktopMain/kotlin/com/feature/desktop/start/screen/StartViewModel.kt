package com.feature.desktop.start.screen

import androidx.lifecycle.ViewModel
import com.network.repositories.contract.QrLoginRepository
import com.network.utils.status.QrSessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing

class StartViewModel(
    private val repository: QrLoginRepository,
): ViewModel() {
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Swing)
    private val _uiState = MutableStateFlow(QrUiState())
    val uiState: StateFlow<QrUiState> = _uiState.asStateFlow()
    private var sessionToken: String? = null
    private var loggedInUserId: String? = null
    private var currentSessionId: String? = null
    private var listenerJob: Job? = null

    fun startQrLoginProcess() {
        if (_uiState.value.loginState != QrLoginState.IDLE && _uiState.value.loginState != QrLoginState.ERROR) return
        _uiState.update {
            it.copy(
                loginState = QrLoginState.GENERATING,
                errorMessage = null,
                qrContent = null
            )
        }
        listenerJob?.cancel()
        currentSessionId = null
        sessionToken = null
        loggedInUserId = null

        viewModelScope.launch {
            repository.createSession()
                .onSuccess { sessionId ->
                    currentSessionId = sessionId
                    _uiState.update {
                        it.copy(
                            qrContent = sessionId,
                            loginState = QrLoginState.DISPLAYING
                        )
                    }
                    startListening(sessionId)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error al crear sesión: ${error.localizedMessage}",
                            loginState = QrLoginState.ERROR
                        )
                    }
                }
        }
    }

    private fun startListening(sessionId: String) {
        listenerJob?.cancel()
        listenerJob = viewModelScope.launch {
            repository.listenToSession(sessionId)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = "Error de conexión: ${error.localizedMessage}",
                            loginState = QrLoginState.ERROR
                        )
                    }
                }
                .collect { status ->
                    println("ViewModel (KMP): Estado recibido: $status")
                    if (_uiState.value.loginState != QrLoginState.ERROR) {
                        when (status) {
                            is QrSessionStatus.Pending -> {
                                if (_uiState.value.loginState != QrLoginState.DISPLAYING) {
                                    _uiState.update {
                                        it.copy(
                                            loginState = QrLoginState.DISPLAYING,
                                            errorMessage = null
                                        )
                                    }
                                }
                            }

                            is QrSessionStatus.Accepted -> {
                                listenerJob?.cancel()
                                println("ViewModel (KMP): Token recibido y almacenado.")
                                sessionToken = status.desktopToken
                                _uiState.update { it.copy(loginState = QrLoginState.SUCCESS) }
                            }

                            is QrSessionStatus.Error -> {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = status.message,
                                        loginState = QrLoginState.ERROR
                                    )
                                }
                                listenerJob?.cancel()
                            }

                            is QrSessionStatus.NotFoundOrExpired -> {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = "Sesión no encontrada o expirada.",
                                        loginState = QrLoginState.ERROR
                                    )
                                }
                                listenerJob?.cancel()
                            }
                        }
                    }
                }
        }
    }

    fun getSessionToken(): String? {
        return sessionToken
    }

    fun getLoggedInUserId(): String? {
        return loggedInUserId
    }

    fun cancelProcess() {
        viewModelScope.launch {
            listenerJob?.cancel()
            val sessionId = currentSessionId
            if (sessionId != null) {
                repository.cancelSession(sessionId)
                    .onFailure { println("ViewModel (KMP): Error al borrar sesión $sessionId: ${it.message}") }
            }
            _uiState.update { QrUiState(loginState = QrLoginState.IDLE) }
            currentSessionId = null
            sessionToken = null
            loggedInUserId = null
        }
    }

    override fun onCleared() {
        println("ViewModel (KMP): onCleared - Cancelando viewModelScope")
        viewModelScope.cancel()
    }
}