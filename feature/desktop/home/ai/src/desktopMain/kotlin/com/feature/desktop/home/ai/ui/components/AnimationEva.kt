package com.feature.desktop.home.ai.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shared.resources.Res
import com.shared.resources.eva_logo
import com.shared.ui.TextStyleBrush
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AnimationEva(
    text: String,
    initiallyVisible: Boolean,
    alpha: Animatable<Float, *>,
    onDisplayedTextChange: (String) -> Unit
) {
    val blockFadeInDurationMillis = 500
    val typingSpeedPerCharMillis = 75L
    LaunchedEffect(key1 = text, key2 = initiallyVisible) {
        if (!initiallyVisible) {
            alpha.snapTo(0f)
            onDisplayedTextChange("")
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = blockFadeInDurationMillis,
                    easing = LinearEasing
                )
            )
            if (blockFadeInDurationMillis == 0 && alpha.value < 1f) alpha.snapTo(1f)

            text.forEachIndexed { index, _ ->
                onDisplayedTextChange(text.substring(0, index + 1))
                delay(typingSpeedPerCharMillis)
            }
        } else {
            alpha.snapTo(1f)
            onDisplayedTextChange(text)
        }
    }
}

@Composable
internal fun Eva(
    displayedText: String,
    alpha: Animatable<Float, *>
) {
    val infiniteTransition = rememberInfiniteTransition()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(Res.drawable.eva_logo),
                contentDescription = "Logo EVA",
                tint = colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = displayedText,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                style = TextStyleBrush(),
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}