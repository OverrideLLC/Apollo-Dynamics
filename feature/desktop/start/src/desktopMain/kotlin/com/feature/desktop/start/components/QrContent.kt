package com.feature.desktop.start.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shared.resources.LogoBlancoQuickness
import com.shared.resources.Res
import com.shared.utils.routes.RoutesStart
import org.jetbrains.compose.resources.painterResource
import qrgenerator.qrkitpainter.QrKitBallShape
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitLogo
import qrgenerator.qrkitpainter.QrKitOptions
import qrgenerator.qrkitpainter.QrKitPixelShape
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPainter
import qrgenerator.qrkitpainter.createRoundCorners
import qrgenerator.qrkitpainter.rememberQrKitPainter
import qrgenerator.qrkitpainter.solidBrush

@Composable
internal fun QrContent(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = qr("www.override.com.mx"),
            contentDescription = "Código QR",
            modifier = Modifier
                .size(300.dp)
                .background(
                    color = colorScheme.background.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .clickable { navController.navigate(RoutesStart.Home.route) },
        )
        Text(
            text = "Escanea el código QR para iniciar sesión.",
            style = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.tertiary
            ),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
internal fun qr(
    token: String,
): QrPainter {
    return QrPainter(
        content = token,
        config = QrKitOptions(
            shapes = QrKitShapes(
                darkPixelShape = QrKitPixelShape.createRoundCorners(),
                ballShape = QrKitBallShape.createRoundCorners(1f)
            ),
            colors = QrKitColors(
                lightBrush = QrKitBrush.solidBrush(color = Color.Transparent),
                ballBrush = QrKitBrush.solidBrush(
                    color = Color(0xff5ea04e)
                ),
                frameBrush = QrKitBrush.solidBrush(
                    color = Color(0xff5ea04e)
                ),
                darkBrush = QrKitBrush.solidBrush(
                    color = Color(0xff5ea04e)
                )
            )
        ),
    )
}