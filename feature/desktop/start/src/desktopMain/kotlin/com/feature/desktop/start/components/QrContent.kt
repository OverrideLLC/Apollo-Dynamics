package com.feature.desktop.start.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.shared.utils.routes.RoutesStart
import qrgenerator.qrkitpainter.rememberQrKitPainter

@Composable
internal fun QrContent(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = rememberQrKitPainter(data = "NJ2N93N293N23U92N3U23N92N3NU239N239U2N3.2I3M293MI23923M29NU3U293.2M93I2N932N39N323NU923"),
            contentDescription = "Código QR",
            modifier = Modifier
                .size(300.dp)
                .clickable { navController.navigate(RoutesStart.Home.route) },
            tint = colorScheme.tertiary
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