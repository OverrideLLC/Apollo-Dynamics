package com.feature.desktop.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.feature.desktop.start.screen.QrUiState
import com.feature.desktop.start.screen.StartViewModel
import com.shared.resources.Res
import com.shared.resources.TTNegro
import com.shared.utils.routes.RoutesStart
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun Content(
    navController: NavController,
    viewModel: StartViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.loginState) {
        if (uiState.loginState == QrLoginState.IDLE) {
            viewModel.startQrLoginProcess()
        }
        if (uiState.loginState == QrLoginState.SUCCESS) {
            navController.navigate(RoutesStart.Home.route)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth(.5f)
            .height(400.dp)
            .background(
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoContent(
                modifier = Modifier.weight(2f)
            )
            VerticalDivider(modifier = Modifier.fillMaxHeight().width(1.dp),)
            qr(
                modifier = Modifier.weight(2f),
                uiState = uiState
            )
        }
    }
}

@Composable
private fun qr(modifier: Modifier, uiState: QrUiState, viewModel: StartViewModel = koinViewModel()) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState.loginState) {
            QrLoginState.IDLE, QrLoginState.GENERATING -> {
                CircularProgressIndicator(color = colorScheme.primary)
            }

            QrLoginState.DISPLAYING -> {
                if (uiState.qrContent != null) {
                    QrDisplay(qrData = uiState.qrContent!!)
                } else {
                    Text("Generando código QR...", color = colorScheme.onSurface)
                }
            }

            QrLoginState.ERROR -> {
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
                Text("¡Inicio de sesión exitoso!", color = colorScheme.primary)
            }
        }
    }
}

@Composable
fun RowScope.VerticalDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.fillMaxHeight().width(1.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
}
