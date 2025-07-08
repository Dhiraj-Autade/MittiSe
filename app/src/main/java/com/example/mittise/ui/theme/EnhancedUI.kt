package com.example.mittise.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.foundation.isSystemInDarkTheme

// Enhanced Card with 3D effects and gradients
@Composable
fun EnhancedCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.cardGradient,
    elevation: Int = 8,
    cornerRadius: Int = 16,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(cornerRadius.dp),
                spotColor = ShadowColors.mediumShadow
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(gradientColors),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .clip(RoundedCornerShape(cornerRadius.dp))
        ) {
            content()
        }
    }
}

// Glass morphism card effect
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 16,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(cornerRadius.dp),
                spotColor = ShadowColors.lightShadow
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
                .clip(RoundedCornerShape(cornerRadius.dp))
        ) {
            content()
        }
    }
}

// Gradient button with 3D effect
@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.primaryGradient,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = ShadowColors.coloredShadow
            ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        content()
    }
}

// Floating action button with gradient
@Composable
fun GradientFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.secondaryGradient,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = ShadowColors.coloredShadow
            ),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(16.dp)
    ) {
        content()
    }
}

// Enhanced text with gradient
@Composable
fun GradientText(
    text: String,
    gradientColors: List<Color> = GradientColors.primaryGradient,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    val isDark = isSystemInDarkTheme()
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = if (isDark) Color.Black else gradientColors.first()
    )
}

// Animated loading card
@Composable
fun AnimatedLoadingCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    EnhancedCard(
        modifier = modifier
            .graphicsLayer(alpha = alpha),
        gradientColors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp)
        ) {
            // Placeholder content
        }
    }
}

// Weather card with glass effect
@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    GlassCard(
        modifier = modifier,
        cornerRadius = 20
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            content()
        }
    }
}

// Market price card with gradient
@Composable
fun MarketPriceCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    EnhancedCard(
        modifier = modifier,
        gradientColors = GradientColors.successGradient,
        elevation = 12,
        cornerRadius = 18
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            content()
        }
    }
}

// Profile card with 3D effect
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    EnhancedCard(
        modifier = modifier,
        gradientColors = GradientColors.tertiaryGradient,
        elevation = 16,
        cornerRadius = 24
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            content()
        }
    }
}

// Navigation item with hover effect
@Composable
fun EnhancedNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    val elevation by animateDpAsState(
        targetValue = if (selected) 8.dp else 2.dp,
        animationSpec = tween(300)
    )
    
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .clickable { onClick() }
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(12.dp),
                spotColor = if (selected) ShadowColors.coloredShadow else ShadowColors.lightShadow
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            label()
        }
    }
}

// Status indicator with pulse animation
@Composable
fun StatusIndicator(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isOnline) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier
            .size(12.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .background(
                color = if (isOnline) Color.Green else Color.Gray,
                shape = RoundedCornerShape(6.dp)
            )
    )
}

// Progress indicator with gradient
@Composable
fun GradientProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.primaryGradient
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = gradientColors.first(),
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

// Enhanced chip with gradient
@Composable
fun GradientChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.secondaryGradient
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowColors.lightShadow
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() }
        ) {
            Text(
                text = text,
                color = if (isDark) Color.Black else Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 