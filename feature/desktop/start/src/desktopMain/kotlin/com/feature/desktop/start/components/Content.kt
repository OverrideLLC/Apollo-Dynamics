package com.feature.desktop.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.feature.desktop.start.screen.QrLoginState
import com.feature.desktop.start.screen.StartViewModel
import com.shared.utils.routes.RoutesStart
import org.koin.compose.viewmodel.koinViewModel

// Asume que tienes un factory o Koin/Hilt configurado para instanciar StartViewModel
// Si no, necesitarás pasar el ViewModel como parámetro desde StartScreen
@Composable
internal fun Content(
    navController: NavController,
    // Puedes inyectar el ViewModel así si usas androidx.lifecycle.viewmodel.compose
    // o pasarlo como parámetro si lo creas en StartScreen
    viewModel: StartViewModel = koinViewModel()
) {
    // Observa el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Llama a startQrLoginProcess solo una vez cuando el estado es IDLE
    // o si hubo un error previo y se quiere reintentar (manejo de reintento no implementado aquí)
    LaunchedEffect(uiState.loginState) {
        if (uiState.loginState == QrLoginState.IDLE) {
            viewModel.startQrLoginProcess()
        }
        // Navega cuando el estado sea SUCCESS
        if (uiState.loginState == QrLoginState.SUCCESS) {
            // Aquí podrías querer obtener el token con viewModel.getSessionToken()
            // y pasarlo a la siguiente pantalla si es necesario.
            println("Navegando a Home...") // Log para depuración
            navController.navigate(RoutesStart.Home.route)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(
                color = colorScheme.onBackground.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            InfoContent()
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxHeight().weight(2f), // Ocupa espacio disponible
                contentAlignment = Alignment.Center
            ) {
                // Muestra contenido diferente según el estado del login QR
                when (uiState.loginState) {
                    QrLoginState.IDLE, QrLoginState.GENERATING -> {
                        // Muestra un indicador de carga mientras se genera
                        CircularProgressIndicator(color = colorScheme.primary)
                    }

                    QrLoginState.DISPLAYING -> {
                        // Muestra el QR si el contenido está listo
                        if (uiState.qrContent != null) {
                            // Llama al composable renombrado que solo muestra el QR
                            QrDisplay(qrData = uiState.qrContent!!)
                        } else {
                            // Estado inconsistente, muestra error o reintenta
                            Text("Generando código QR...", color = colorScheme.onSurface)
                        }
                    }

                    QrLoginState.ERROR -> {
                        // Muestra mensaje de error y un botón para reintentar
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.errorMessage ?: "Error desconocido",
                                color = colorScheme.error,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(onClick = { viewModel.startQrLoginProcess() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    QrLoginState.LOGGING_IN -> { // Podrías tener este estado si hay un paso intermedio
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = colorScheme.primary)
                            Text(
                                "Verificando inicio de sesión...",
                                color = colorScheme.onSurface,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    QrLoginState.SUCCESS -> {
                        // Muestra un mensaje de éxito brevemente antes de navegar (la navegación se maneja en LaunchedEffect)
                        Text("¡Inicio de sesión exitoso!", color = colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
