package com.feature.desktop.start.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Eliminado - Ya no navega desde aquí
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.material3.Icon // No se usa
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shared.resources.LogoBlancoQuickness
// import androidx.navigation.NavController // Eliminado
import com.shared.resources.Res // Importado correctamente
import com.shared.resources.logo_quickness_redondeado // Importado correctamente
// import com.shared.utils.routes.RoutesStart // Eliminado
import org.jetbrains.compose.resources.painterResource
import qrgenerator.qrkitpainter.QrKitBallShape
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitErrorCorrection
import qrgenerator.qrkitpainter.QrKitLogo
import qrgenerator.qrkitpainter.QrKitLogoKitShape
import qrgenerator.qrkitpainter.QrKitLogoPadding
import qrgenerator.qrkitpainter.QrKitOptions
import qrgenerator.qrkitpainter.QrKitPixelShape
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPainter
import qrgenerator.qrkitpainter.createCircle
import qrgenerator.qrkitpainter.createRoundCorners
// import qrgenerator.qrkitpainter.rememberQrKitPainter // No necesario si creamos QrPainter directamente
import qrgenerator.qrkitpainter.solidBrush

/**
 * Composable que muestra específicamente la imagen del código QR y un texto debajo.
 * @param qrData El String que se codificará en el QR.
 */
@Composable
internal fun QrDisplay(qrData: String) { // Cambiado el nombre y parámetro
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            // Llama a la función helper 'qr' con el dato dinámico
            painter = qr(token = qrData), // Usa el parámetro qrData
            contentDescription = "Código QR de inicio de sesión", // Descripción más específica
            modifier = Modifier
                .size(300.dp) // Puedes ajustar el tamaño si es necesario
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
            // .clickable { navController.navigate(RoutesStart.Home.route) }, // Eliminado - La navegación se maneja en Content.kt
        )
        Text(
            text = "Escanea el código QR para iniciar sesión.",
            style = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.tertiary // Asegúrate que este color tenga buen contraste
            ),
            modifier = Modifier.padding(top = 16.dp) // Aumentado el padding superior
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
            errorCorrection = QrKitErrorCorrection.High,
            colors = QrKitColors(
                lightBrush = QrKitBrush.solidBrush(color = Color.Transparent),
                ballBrush = QrKitBrush.solidBrush(
                    color = Color(0xff000000)
                ),
                frameBrush = QrKitBrush.solidBrush(
                    color = Color(0xff000000)
                ),
                darkBrush = QrKitBrush.solidBrush(
                    color = Color(0xff000000)
                )
            ),
        ),
    )
}