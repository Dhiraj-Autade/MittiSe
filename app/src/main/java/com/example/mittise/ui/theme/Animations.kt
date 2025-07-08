package com.example.mittise.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

// Animation durations
object AnimationDurations {
    const val FAST = 200
    const val MEDIUM = 300
    const val SLOW = 500
    const val VERY_SLOW = 800
}

// Animation curves
object AnimationCurves {
    val EaseInOut = FastOutSlowInEasing
    val EaseOut = FastOutLinearInEasing
    val EaseIn = LinearOutSlowInEasing
    val Bounce = FastOutSlowInEasing
}

// Fade in animation
@Composable
fun FadeInAnimation(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            )
        ),
        content = content
    )
}

// Slide in animation
@Composable
fun SlideInAnimation(
    visible: Boolean,
    slideDirection: SlideDirection = SlideDirection.Up,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            ),
            initialOffsetY = { if (slideDirection == SlideDirection.Up) it else -it }
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            )
        ),
        exit = slideOutVertically(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            ),
            targetOffsetY = { if (slideDirection == SlideDirection.Up) -it else it }
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            )
        ),
        content = content
    )
}

// Scale animation
@Composable
fun ScaleAnimation(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            ),
            initialScale = 0.8f
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            )
        ),
        exit = scaleOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            ),
            targetScale = 0.8f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            )
        ),
        content = content
    )
}

// Pulse animation
@Composable
fun PulseAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = AnimationCurves.EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    ) {
        content()
    }
}

// Floating animation
@Composable
fun FloatingAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = AnimationCurves.EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier.offset(y = offsetY.dp)
    ) {
        content()
    }
}

// Shimmer animation
@Composable
fun ShimmerAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = AnimationCurves.EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier.graphicsLayer(alpha = alpha)
    ) {
        content()
    }
}

// 3D Rotation animation
@Composable
fun Rotation3DAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = AnimationCurves.EaseInOut
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Box(
        modifier = Modifier.graphicsLayer(
            rotationY = rotationY
        )
    ) {
        content()
    }
}

// Bounce animation
@Composable
fun BounceAnimation(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.Bounce
            ),
            initialScale = 0.3f
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM,
                easing = AnimationCurves.EaseInOut
            )
        ),
        exit = scaleOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            ),
            targetScale = 0.3f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            )
        ),
        content = content
    )
}

// Staggered animation for lists
@Composable
fun StaggeredAnimation(
    visible: Boolean,
    index: Int,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM + (index * 100),
                easing = AnimationCurves.EaseInOut
            ),
            initialOffsetY = { it }
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationDurations.MEDIUM + (index * 100),
                easing = AnimationCurves.EaseInOut
            )
        ),
        exit = slideOutVertically(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            ),
            targetOffsetY = { it }
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationDurations.FAST,
                easing = AnimationCurves.EaseOut
            )
        ),
        content = content
    )
}

// Loading spinner animation
@Composable
fun LoadingSpinnerAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Box(
        modifier = Modifier.graphicsLayer(
            rotationZ = rotation
        )
    ) {
        content()
    }
}

// Heartbeat animation
@Composable
fun HeartbeatAnimation(
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = AnimationCurves.EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    ) {
        content()
    }
}

// Slide direction enum
enum class SlideDirection {
    Up, Down, Left, Right
} 