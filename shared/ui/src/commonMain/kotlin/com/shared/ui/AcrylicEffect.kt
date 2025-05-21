package com.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * AcrylicEffect is a composable that applies a frosted glass effect to its content.
 * 
 * @param modifier The modifier to be applied to the layout
 * @param blurRadius The radius of the blur effect (in dp)
 * @param alpha The transparency level of the effect (0.0f - 1.0f)
 * @param cornerRadius The corner radius of the surface (in dp)
 * @param backgroundColor The base color for the acrylic effect
 * @param content The content to be displayed with the acrylic effect
 */
@Composable
fun AcrylicEffect(
    modifier: Modifier = Modifier,
    blurRadius: Float = 20f,
    alpha: Float = 0.5f,
    cornerRadius: Int = 16,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // The blurred background
        Surface(
            modifier = Modifier.matchParentSize(),
            shape = RoundedCornerShape(cornerRadius.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .blur(blurRadius.dp)
                    .alpha(alpha)
                    .background(backgroundColor)
            )
        }
        
        // Content layer
        Box(
            modifier = Modifier.matchParentSize(),
            content = content
        )
    }
}

/**
 * AcrylicCard is a pre-styled card with acrylic effect.
 * 
 * @param modifier The modifier to be applied to the layout
 * @param blurRadius The radius of the blur effect (in dp)
 * @param alpha The transparency level of the effect (0.0f - 1.0f)
 * @param cornerRadius The corner radius of the card (in dp)
 * @param content The content to be displayed inside the card
 */
@Composable
fun AcrylicCard(
    modifier: Modifier = Modifier,
    blurRadius: Float = 15f,
    alpha: Float = 0.6f,
    cornerRadius: Int = 16,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        AcrylicEffect(
            blurRadius = blurRadius,
            alpha = alpha,
            cornerRadius = cornerRadius,
            backgroundColor = colorScheme.surface,
            content = content
        )
    }
}

/**
 * AcrylicGradient provides an acrylic effect with a gradient background.
 * 
 * @param modifier The modifier to be applied to the layout
 * @param blurRadius The radius of the blur effect (in dp)
 * @param alpha The transparency level of the effect (0.0f - 1.0f)
 * @param cornerRadius The corner radius of the surface (in dp)
 * @param gradientColors List of colors for the gradient background
 * @param content The content to be displayed with the acrylic effect
 */
@Composable
fun AcrylicGradient(
    modifier: Modifier = Modifier,
    blurRadius: Float = 20f,
    alpha: Float = 0.5f,
    cornerRadius: Int = 16,
    gradientColors: List<Color> = listOf(
        Color(0x80FFFFFF),
        Color(0x40FFFFFF)
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // The blurred gradient background
        Surface(
            modifier = Modifier.matchParentSize(),
            shape = RoundedCornerShape(cornerRadius.dp),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .blur(blurRadius.dp)
                    .alpha(alpha)
                    .background(
                        Brush.verticalGradient(gradientColors)
                    )
            )
        }
        
        // Content layer
        Box(
            modifier = Modifier.matchParentSize(),
            content = content
        )
    }
}

/**
 * Sample usage demonstration
 */
@Composable
fun AcrylicEffectDemo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3700B3),
                        Color(0xFF6200EE)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic Acrylic Effect
            AcrylicEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                blurRadius = 15f,
                alpha = 0.7f
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(
                        "Basic Acrylic Effect",
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
            
            // Acrylic Card
            AcrylicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(
                        "Acrylic Card",
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
            
            // Acrylic Gradient
            AcrylicGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                gradientColors = listOf(
                    Color(0x80E91E63),
                    Color(0x40FFC107)
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(
                        "Acrylic Gradient",
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
        }
    }
}
