package com.example.mittise.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Enhanced Light Theme Colors with beautiful gradients
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),           // Deep Forest Green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4CAF50),  // Vibrant Green
    onPrimaryContainer = Color(0xFFFFFFFF),
    
    secondary = Color(0xFFFF9800),         // Warm Orange
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFFFB74D), // Light Orange
    onSecondaryContainer = Color(0xFF000000),
    
    tertiary = Color(0xFF2196F3),          // Beautiful Blue
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF64B5F6), // Light Blue
    onTertiaryContainer = Color(0xFF000000),
    
    error = Color(0xFFE53935),             // Vibrant Red
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),    // Light Red
    onErrorContainer = Color(0xFF000000),
    
    background = Color(0xFFF8F9FA),        // Soft Light Background
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    
    surfaceVariant = Color(0xFFF5F5F5),    // Subtle Gray
    onSurfaceVariant = Color(0xFF424242),
    
    outline = Color(0xFF9E9E9E),           // Medium Gray
    outlineVariant = Color(0xFFE0E0E0),    // Light Gray
    
    scrim = Color(0x80000000),
    inverseSurface = Color(0xFF121212),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF4CAF50)
)

// Enhanced Dark Theme Colors with rich gradients
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),           // Vibrant Green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF2E7D32),  // Deep Green
    onPrimaryContainer = Color(0xFFFFFFFF),
    
    secondary = Color(0xFFFFB74D),         // Warm Orange
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFFF9800), // Deep Orange
    onSecondaryContainer = Color(0xFF000000),
    
    tertiary = Color(0xFF64B5F6),          // Light Blue
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF2196F3), // Deep Blue
    onTertiaryContainer = Color(0xFFFFFFFF),
    
    error = Color(0xFFFFCDD2),             // Light Red
    onError = Color(0xFF000000),
    errorContainer = Color(0xFFE53935),    // Deep Red
    onErrorContainer = Color(0xFFFFFFFF),
    
    background = Color(0xFF0A0A0A),        // Deep Dark Background
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF1A1A1A),           // Dark Surface
    onSurface = Color(0xFFF5F5F5),
    
    surfaceVariant = Color(0xFF2D2D2D),    // Dark Gray
    onSurfaceVariant = Color(0xFFE0E0E0),
    
    outline = Color(0xFF757575),           // Medium Gray
    outlineVariant = Color(0xFF424242),    // Dark Gray
    
    scrim = Color(0x80000000),
    inverseSurface = Color(0xFFF8F9FA),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = Color(0xFF4CAF50)
)

// Custom gradient colors for enhanced visual appeal
object GradientColors {
    val primaryGradient = listOf(
        Color(0xFF2E7D32),  // Deep Green
        Color(0xFF4CAF50),  // Vibrant Green
        Color(0xFF66BB6A)   // Light Green
    )
    
    val secondaryGradient = listOf(
        Color(0xFFFF9800),  // Orange
        Color(0xFFFFB74D),  // Light Orange
        Color(0xFFFFCC80)   // Very Light Orange
    )
    
    val tertiaryGradient = listOf(
        Color(0xFF2196F3),  // Blue
        Color(0xFF64B5F6),  // Light Blue
        Color(0xFF90CAF9)   // Very Light Blue
    )
    
    val successGradient = listOf(
        Color(0xFF4CAF50),  // Green
        Color(0xFF66BB6A),  // Light Green
        Color(0xFF81C784)   // Very Light Green
    )
    
    val warningGradient = listOf(
        Color(0xFFFF9800),  // Orange
        Color(0xFFFFB74D),  // Light Orange
        Color(0xFFFFCC80)   // Very Light Orange
    )
    
    val errorGradient = listOf(
        Color(0xFFE53935),  // Red
        Color(0xFFEF5350),  // Light Red
        Color(0xFFE57373)   // Very Light Red
    )
    
    val cardGradient = listOf(
        Color(0xFFFFFFFF),  // White
        Color(0xFFF8F9FA),  // Very Light Gray
        Color(0xFFF5F5F5)   // Light Gray
    )
    
    val darkCardGradient = listOf(
        Color(0xFF2D2D2D),  // Dark Gray
        Color(0xFF1A1A1A),  // Darker Gray
        Color(0xFF0A0A0A)   // Very Dark Gray
    )
}

// 3D effect colors for depth and shadows
object ShadowColors {
    val lightShadow = Color(0x1A000000)    // Subtle shadow
    val mediumShadow = Color(0x33000000)   // Medium shadow
    val heavyShadow = Color(0x4D000000)    // Heavy shadow
    val coloredShadow = Color(0x1A2E7D32)  // Green tinted shadow
}

@Composable
fun MittiSeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match primary color
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 