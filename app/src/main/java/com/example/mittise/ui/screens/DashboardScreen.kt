package com.example.mittise.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.mittise.R
import com.example.mittise.model.Category
import com.example.mittise.model.Update
import com.example.mittise.ui.theme.*
import com.example.mittise.ui.weather.WeatherViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log

@Composable
fun DashboardScreen(
    onNavigateToWeather: () -> Unit,
    onNavigateToCropCalendar: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToArticles: () -> Unit
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            WelcomeHeader()
        }
        item {
            EnhancedWeatherCard(
                onWeatherClick = onNavigateToWeather
            )
        }
        item {
            Column {
                GradientText(
                    text = stringResource(R.string.dashboard_quick_actions),
                    gradientColors = GradientColors.primaryGradient,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                CategoriesSection(
                    categories = getDummyCategories(context),
                    onCategoryClick = { category ->
                        when (category.name) {
                            context.getString(R.string.category_seeds) -> onNavigateToCropCalendar()
                            context.getString(R.string.language) -> onNavigateToLanguage()
                            context.getString(R.string.category_equipment) -> onNavigateToHelp()
                            context.getString(R.string.category_pesticides) -> onNavigateToFeedback()
                        }
                    }
                )
            }
        }
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GradientText(
                        text = stringResource(R.string.updates_title),
                        gradientColors = GradientColors.secondaryGradient,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    GradientButton(
                        onClick = onNavigateToArticles,
                        gradientColors = GradientColors.tertiaryGradient
                    ) {
                        Text(
                            text = stringResource(R.string.view_all),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        items(getDummyUpdates(context)) { update ->
            EnhancedUpdateCard(
                update = update,
                onClick = { /* Handle update click */ }
            )
        }
    }
}

@Composable
fun WelcomeHeader() {
    EnhancedCard(
        gradientColors = GradientColors.primaryGradient,
        elevation = 12,
        cornerRadius = 20
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Icon
            FloatingAnimation {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = stringResource(R.string.dashboard_welcome),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Your farming companion",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun EnhancedWeatherCard(
    onWeatherClick: () -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        Log.d("WeatherCard", "Permission result: FINE=$fineLocationGranted, COARSE=$coarseLocationGranted")
        
        if (fineLocationGranted || coarseLocationGranted) {
            weatherViewModel.checkLocationPermission(context)
        }
    }
    
    // Check location permission on first launch
    LaunchedEffect(Unit) {
        Log.d("WeatherCard", "LaunchedEffect: Checking permission")
        weatherViewModel.checkLocationPermission(context)
        
        // If permission not granted, request it
        if (!weatherViewModel.hasLocationPermission) {
            Log.d("WeatherCard", "LaunchedEffect: Requesting permission")
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    
    // Get location and weather data when permission is granted
    LaunchedEffect(weatherViewModel.hasLocationPermission) {
        Log.d("WeatherCard", "LaunchedEffect hasLocationPermission: ${weatherViewModel.hasLocationPermission}")
        if (weatherViewModel.hasLocationPermission) {
            weatherViewModel.getCurrentLocation(context)
        }
    }
    
    // Refresh weather data when app becomes active
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (weatherViewModel.hasLocationPermission && weatherViewModel.location != null) {
                weatherViewModel.refreshWeather()
            }
        }
    }
    
    WeatherCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onWeatherClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Weather Icon
            PulseAnimation {
                LottieAnimation(
                    composition = rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.weather_sunny)
                    ).value,
                    isPlaying = true,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(80.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            // Weather Info with Gradient Text
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_weather_sunny),
                        contentDescription = "Weather",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GradientText(
                        text = when {
                            weatherViewModel.isLoading -> "Loading..."
                            weatherViewModel.weatherData?.main?.temp != null -> 
                                "${weatherViewModel.weatherData!!.main!!.temp.toInt()}°C"
                            else -> "N/A"
                        },
                        gradientColors = GradientColors.primaryGradient,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Text(
                    text = when {
                        weatherViewModel.isLoading -> "Getting location..."
                        weatherViewModel.weatherData?.name != null -> 
                            weatherViewModel.weatherData!!.name!!
                        weatherViewModel.error != null -> "Location error"
                        !weatherViewModel.hasLocationPermission -> "Enable location"
                        weatherViewModel.location == null -> "Getting location..."
                        else -> "Location available"
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Weather Details with Enhanced Design
            Column(
                horizontalAlignment = Alignment.End
            ) {
                WeatherDetailItem(
                    icon = Icons.Default.Opacity,
                    value = when {
                        weatherViewModel.isLoading -> "--"
                        weatherViewModel.weatherData?.main?.humidity != null -> 
                            "${weatherViewModel.weatherData!!.main!!.humidity}%"
                        else -> "N/A"
                    },
                    label = "Humidity"
                )
                Spacer(modifier = Modifier.height(8.dp))
                WeatherDetailItem(
                    icon = Icons.Default.Air,
                    value = when {
                        weatherViewModel.isLoading -> "--"
                        weatherViewModel.weatherData?.wind?.speed != null -> 
                            "${weatherViewModel.weatherData!!.wind!!.speed.toInt()} km/h"
                        else -> "N/A"
                    },
                    label = "Wind"
                )
            }
        }
    }
}

@Composable
fun WeatherDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(categories) { category ->
            StaggeredAnimation(
                visible = true,
                index = categories.indexOf(category)
            ) {
                EnhancedCategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
fun EnhancedCategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    EnhancedCard(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() },
        gradientColors = GradientColors.secondaryGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Icon
            HeartbeatAnimation {
                Icon(
                    painter = painterResource(id = category.iconResId),
                    contentDescription = category.name,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun UpdatesSection(
    updates: List<Update>,
    onUpdateClick: (Update) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(updates) { update ->
            StaggeredAnimation(
                visible = true,
                index = updates.indexOf(update)
            ) {
                EnhancedUpdateCard(
                    update = update,
                    onClick = { onUpdateClick(update) }
                )
            }
        }
    }
}

@Composable
fun EnhancedUpdateCard(
    update: Update,
    onClick: () -> Unit
) {
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        gradientColors = GradientColors.tertiaryGradient,
        elevation = 6,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Update Icon with Rotation
            Rotation3DAnimation {
                Icon(
                    painter = painterResource(id = update.imageResId),
                    contentDescription = update.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = update.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = update.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Status Indicator
            StatusIndicator(
                isOnline = true,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

private fun getDummyCategories(context: android.content.Context): List<Category> {
    return listOf(
        Category(
            id = 1,
            name = context.getString(R.string.category_seeds),
            iconResId = R.drawable.ic_seeds
        ),
        Category(
            id = 2,
            name = context.getString(R.string.language),
            iconResId = R.drawable.ic_language
        ),
        Category(
            id = 3,
            name = context.getString(R.string.category_equipment),
            iconResId = R.drawable.ic_equipment
        ),
        Category(
            id = 4,
            name = context.getString(R.string.category_pesticides),
            iconResId = R.drawable.ic_pesticide
        )
    )
}

private fun getDummyUpdates(context: android.content.Context): List<Update> {
    return listOf(
        Update(
            id = 1,
            title = context.getString(R.string.update_title_crop_advisory),
            description = context.getString(R.string.update_desc_crop_advisory),
            date = context.getString(R.string.update_date_today),
            imageResId = R.drawable.ic_news
        ),
        Update(
            id = 2,
            title = context.getString(R.string.update_title_market_price),
            description = context.getString(R.string.update_desc_market_price),
            date = context.getString(R.string.update_date_today),
            imageResId = R.drawable.ic_marketplace
        ),
        Update(
            id = 3,
            title = context.getString(R.string.update_title_weather_alert),
            description = context.getString(R.string.update_desc_weather_alert),
            date = context.getString(R.string.update_date_today),
            imageResId = R.drawable.ic_weather
        )
    )
} 