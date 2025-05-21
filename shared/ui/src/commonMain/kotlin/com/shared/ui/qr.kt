package com.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
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
import qrgenerator.qrkitpainter.solidBrush

@Composable
fun qr(
    token: String,
    icon: DrawableResource
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
                    color = Color(0xffffffff)
                ),
                frameBrush = QrKitBrush.solidBrush(
                    color = Color(0xffffffff)
                ),
                darkBrush = QrKitBrush.solidBrush(
                    color = Color(0xffffffff)
                )
            ),
            logo = QrKitLogo(
                painter = painterResource(icon),
                size = 0.3f,
                padding = QrKitLogoPadding.Exact(0.1f),
                shape = QrKitLogoKitShape.createCircle()
            )
        ),
    )
}