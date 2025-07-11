package com.example.mittise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mittise.R
import com.example.mittise.ui.theme.*
import com.example.mittise.ui.viewmodels.ProfileViewModel
import com.example.mittise.ui.viewmodels.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mittise.ui.viewmodels.ChatbotViewModel
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.graphics.vector.ImageVector

// APMC Screen - Agricultural Produce Market Committee
@Composable
fun ApmcScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.primaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.apmc_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.apmc_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Market Prices Section
        item {
            Text(
                text = "Today's Market Prices",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Sample Market Prices
        items(getSampleMarketPrices()) { price ->
            MarketPriceCard(price = price)
        }

        // Market Information
        item {
            EnhancedCard(
                gradientColors = GradientColors.tertiaryGradient,
                elevation = 8,
                cornerRadius = 16
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Market Information",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "APMC prices are updated daily. Prices may vary based on market conditions, quality, and demand.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }
    }
}

// Chatbot Screen - AI Assistant for Farmers
@Composable
fun ChatbotScreen() {
    val viewModel: ChatbotViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val recognizedText by viewModel.recognizedText.collectAsState()
    
    var messageText by remember { mutableStateOf("") }
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, can start speech recognition
            viewModel.toggleVoiceInput()
        }
    }
    
    // Update messageText when recognizedText changes
    LaunchedEffect(recognizedText) {
        if (recognizedText.isNotEmpty()) {
            messageText = recognizedText
        }
    }
    
    // Set context for speech recognition
    LaunchedEffect(context) {
        viewModel.setContext(context)
    }
    
    Column(
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
    ) {
        // Header
        EnhancedCard(
            gradientColors = GradientColors.secondaryGradient,
            elevation = 12,
            cornerRadius = 20,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SmartToy,
                                contentDescription = "AI Assistant",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "MittiSe AI Assistant",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Your farming companion",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) Color.Black.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
        
        // Chat Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    WelcomeMessage()
                }
            } else {
                items(messages.reversed()) { message ->
                    ChatMessageItem(
                        message = message, 
                        isDark = isDark,
                        onSpeakClick = { viewModel.speakMessage(message.text) }
                    )
                }
            }
            
            if (isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }
        
        // Input Section
        ChatInputSection(
            messageText = messageText,
            onMessageTextChange = { messageText = it },
            onSendClick = {
                if (messageText.isNotBlank()) {
                    viewModel.sendMessage(messageText)
                    messageText = ""
                    viewModel.clearRecognizedText()
                }
            },
            onVoiceClick = {
                // Check permission before starting voice input
                if (viewModel.isMicrophonePermissionGranted()) {
                    viewModel.toggleVoiceInput()
                } else {
                    launcher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            },
            isListening = isListening,
            isDark = isDark
        )
    }
}

@Composable
fun WelcomeMessage() {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 16
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.chatbot_welcome_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.chatbot_welcome_message),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.Black.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.chatbot_suggestions),
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, isDark: Boolean, onSpeakClick: () -> Unit) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        if (isDark) Color.White else MaterialTheme.colorScheme.surface
    }
    val textColor = if (message.isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatTimestamp(message.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f)
                    )
                    if (!message.isUser) {
                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VolumeUp,
                                contentDescription = "Speak",
                                tint = textColor.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val isDark = isSystemInDarkTheme()
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    if (index < 2) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputSection(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onVoiceClick: () -> Unit,
    isListening: Boolean,
    isDark: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Voice Button
            IconButton(
                onClick = onVoiceClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Filled.Mic else Icons.Filled.MicNone,
                    contentDescription = if (isListening) "Stop Recording" else "Start Recording",
                    tint = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Text Input
            TextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = if (isListening) "Listening..." else "Type your question...",
                        color = if (isDark) Color.Black.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                maxLines = 4
            )
            
            // Send Button
            IconButton(
                onClick = onSendClick,
                enabled = messageText.isNotBlank(),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Profile Screen
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    
    // Navigate to login if user logs out while on profile screen
    LaunchedEffect(authState.isLoggedIn) {
        if (!authState.isLoggedIn) {
            onNavigateToLogin()
        }
    }
    
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.tertiaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(40.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (uiState.isLoading && authState.isLoggedIn) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else if (authState.isLoggedIn) {
                        Text(
                            text = profileViewModel.getDisplayName(),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Active Farmer",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )
                        )
                    } else {
                        Text(
                            text = "Guest User",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Please login to access your profile",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }

        // Profile Actions (only for logged-in users)
        if (authState.isLoggedIn) {
            item {
                Button(
                    onClick = { /* Navigate to edit profile */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.profile_edit),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.profile_edit))
                }
            }
        }

        // Profile Stats (only for logged-in users)
        if (authState.isLoggedIn) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatCard(
                        title = stringResource(R.string.profile_posts),
                        value = "12",
                        icon = Icons.Filled.Article
                    )
                    ProfileStatCard(
                        title = stringResource(R.string.profile_followers),
                        value = "45",
                        icon = Icons.Filled.People
                    )
                    ProfileStatCard(
                        title = stringResource(R.string.profile_following),
                        value = "23",
                        icon = Icons.Filled.People
                    )
                }
            }
        }

        // Profile Options
        items(getProfileOptions(profileViewModel, authState.isLoggedIn, onNavigateToLogin)) { option ->
            ProfileOptionCard(option = option)
        }
    }
    
    // Logout Confirmation Dialog (only for logged-in users)
    if (authState.isLoggedIn && uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { profileViewModel.hideLogoutDialog() },
            title = {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = profileViewModel.logout()
                        if (success) {
                            // Navigation will be handled by the auth state change
                        }
                    }
                ) {
                    Text(
                        text = "Yes",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { profileViewModel.hideLogoutDialog() }
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
    
    // Error handling
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // You can show a snackbar here if needed
            profileViewModel.clearErrorMessage()
        }
    }
}

// Weather Screen
@Composable
fun WeatherScreen(
    weatherViewModel: com.example.mittise.ui.weather.WeatherViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current

    // Check location permission and services on first launch and when coming back
    androidx.compose.runtime.LaunchedEffect(Unit) {
        weatherViewModel.checkLocationPermission(context)
        weatherViewModel.checkLocationServicesEnabled(context)
    }
    // Also check location services whenever the screen resumes
    androidx.compose.runtime.LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            weatherViewModel.checkLocationServicesEnabled(context)
            if (weatherViewModel.hasLocationPermission && weatherViewModel.location != null && weatherViewModel.isLocationServiceEnabled) {
                weatherViewModel.refreshWeather()
            }
        }
    }

    // Show prompt if location services are OFF
    if (!weatherViewModel.isLocationServiceEnabled) {
        LocationServicePrompt(onEnable = {
            val intent = android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.primaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.weather_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Current weather and forecast for your location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Current Weather
        item {
            CurrentWeatherCard(weatherViewModel = weatherViewModel)
        }

        // Weather Analysis
        if (weatherViewModel.weatherAnalysis != null) {
            item {
                WeatherAnalysisCard(analysis = weatherViewModel.weatherAnalysis!!)
            }
        }

        // Weather Forecast
        item {
            Text(
                text = "5-Day Forecast",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Display live forecast data
        if (weatherViewModel.forecastData?.forecastList != null) {
            // Group forecast by day and take first entry for each day
            val dailyForecasts = weatherViewModel.forecastData!!.forecastList!!.groupBy { forecast ->
                forecast.dtTxt.split(" ")[0] // Get date part
            }.values.map { it.first() }.take(5) // Take first 5 days
            
            items(dailyForecasts) { forecast ->
                LiveWeatherForecastCard(forecast = forecast)
            }
        } else if (weatherViewModel.isLoading) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Loading forecast...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            // Show sample data if no live data
            items(getWeatherForecast()) { forecast ->
                WeatherForecastCard(forecast = forecast)
            }
        }
    }
}

@Composable
fun LocationServicePrompt(onEnable: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOff,
                    contentDescription = "Location Off",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.location_turned_off),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.enable_location_services),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onEnable) {
                    Text(stringResource(R.string.turn_on_location))
                }
            }
        }
    }
}

// Crop Calendar Screen
@Composable
fun CropCalendarScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.successGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.crop_calendar_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.crop_calendar_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Current Month Activities
        item {
            Text(
                text = stringResource(R.string.this_month_activities),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(getCropCalendarActivities()) { activity ->
            CropActivityCard(activity = activity)
        }

        // Seasonal Tips
        item {
            SeasonalTipsCard()
        }
    }
}

// Soil Testing Screen
@Composable
fun SoilTestingScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.warningGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.soil_testing_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.soil_testing_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Test Soil Button
        item {
            Button(
                onClick = { /* Handle soil testing */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Science,
                    contentDescription = "Test Soil",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.test_your_soil))
            }
        }

        // Soil Testing Options
        items(getSoilTestingOptions()) { option ->
            SoilTestingOptionCard(option = option)
        }

        // Previous Test Results
        item {
            PreviousTestResultsCard()
        }
    }
}

// Articles Screen
@Composable
fun ArticlesScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.tertiaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.articles_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.articles_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Featured Articles
        items(getSampleArticles()) { article ->
            ArticleCard(article = article)
        }
    }
}

// Advisor Screen - AI Farming Advisor
@Composable
fun AdvisorScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.primaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.advisor),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "AI-powered farming advice and recommendations",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Ask Question Button
        item {
            Button(
                onClick = { /* Handle ask question */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Chat,
                    contentDescription = "Ask Question",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.ask_ai_advisor))
            }
        }

        // Recent Recommendations
        items(getAdvisorRecommendations()) { recommendation ->
            AdvisorRecommendationCard(recommendation = recommendation)
        }
    }
}

// Schemes Screen - Government Schemes
@Composable
fun SchemesScreen() {
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EnhancedCard(
                gradientColors = GradientColors.successGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.schemes_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.schemes_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Available Schemes
        items(getGovernmentSchemes()) { scheme ->
            GovernmentSchemeCard(scheme = scheme)
        }
    }
}

// Data Classes and Sample Data
data class MarketPrice(
    val crop: String,
    val price: String,
    val unit: String,
    val market: String,
    val change: String
)

data class CommunityPost(
    val id: String,
    val author: String,
    val content: String,
    val likes: Int,
    val comments: Int,
    val timeAgo: String
)

data class ProfileOption(
    val titleRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

data class WeatherForecast(
    val day: String,
    val temperature: String,
    val condition: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class CropActivity(
    val crop: String,
    val activity: String,
    val date: String,
    val status: String
)

data class SoilTestingOption(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class Article(
    val title: String,
    val summary: String,
    val author: String,
    val readTime: String
)

data class AdvisorRecommendation(
    val title: String,
    val description: String,
    val category: String
)

data class GovernmentScheme(
    val title: String,
    val description: String,
    val eligibility: String,
    val benefits: String
)

// Sample Data Functions
private fun getSampleMarketPrices(): List<MarketPrice> = listOf(
    MarketPrice("Wheat", "₹2,100", "per quintal", "Mumbai APMC", "+₹50"),
    MarketPrice("Rice", "₹1,800", "per quintal", "Delhi APMC", "-₹30"),
    MarketPrice("Cotton", "₹6,500", "per quintal", "Ahmedabad APMC", "+₹100"),
    MarketPrice("Sugarcane", "₹315", "per quintal", "Pune APMC", "No change")
)

private fun getSampleCommunityPosts(): List<CommunityPost> = listOf(
    CommunityPost("1", "Rajesh Kumar", "Just harvested my wheat crop. Great yield this year! 🌾", 12, 5, "2 hours ago"),
    CommunityPost("2", "Lakshmi Devi", "Looking for advice on organic pest control for tomatoes. Any suggestions?", 8, 15, "5 hours ago"),
    CommunityPost("3", "Gurpreet Singh", "Weather forecast shows rain next week. Perfect for sowing rice! ☔", 20, 3, "1 day ago")
)

private fun getProfileOptions(
    profileViewModel: ProfileViewModel, 
    isLoggedIn: Boolean, 
    onNavigateToLogin: () -> Unit
): List<ProfileOption> = listOf(
    ProfileOption(R.string.profile_settings, Icons.Filled.Settings) { },
    ProfileOption(R.string.profile_notifications, Icons.Filled.Notifications) { },
    ProfileOption(R.string.profile_privacy, Icons.Filled.Security) { },
    ProfileOption(R.string.profile_help, Icons.Filled.Help) { },
    if (isLoggedIn) {
        ProfileOption(R.string.profile_logout, Icons.Filled.Logout) { 
            profileViewModel.showLogoutDialog()
        }
    } else {
        ProfileOption(R.string.profile_login, Icons.Filled.Login) { 
            onNavigateToLogin()
        }
    }
)

private fun getWeatherForecast(): List<WeatherForecast> = listOf(
    WeatherForecast("Today", "28°C", "Sunny", Icons.Filled.WbSunny),
    WeatherForecast("Tomorrow", "26°C", "Partly Cloudy", Icons.Filled.Cloud),
    WeatherForecast("Wed", "24°C", "Rain", Icons.Filled.Opacity),
    WeatherForecast("Thu", "27°C", "Sunny", Icons.Filled.WbSunny),
    WeatherForecast("Fri", "29°C", "Sunny", Icons.Filled.WbSunny)
)

private fun getCropCalendarActivities(): List<CropActivity> = listOf(
    CropActivity("Wheat", "Harvesting", "15-20 July", "In Progress"),
    CropActivity("Rice", "Sowing", "25-30 July", "Upcoming"),
    CropActivity("Cotton", "Pest Control", "10-15 July", "Completed")
)

private fun getSoilTestingOptions(): List<SoilTestingOption> = listOf(
    SoilTestingOption("pH Testing", "Check soil acidity/alkalinity", Icons.Filled.Science),
    SoilTestingOption("Nutrient Analysis", "Test NPK levels", Icons.Filled.Science),
    SoilTestingOption("Organic Matter", "Check soil organic content", Icons.Filled.Science)
)

private fun getSampleArticles(): List<Article> = listOf(
    Article("Modern Farming Techniques", "Learn about the latest farming methods that can increase your yield.", "Dr. Agriculture", "5 min read"),
    Article("Organic Farming Guide", "Complete guide to organic farming practices and certification.", "Organic Expert", "8 min read"),
    Article("Crop Rotation Benefits", "Understanding the importance of crop rotation for soil health.", "Soil Scientist", "6 min read")
)

private fun getAdvisorRecommendations(): List<AdvisorRecommendation> = listOf(
    AdvisorRecommendation("Optimal Sowing Time", "Based on current weather, sow rice between July 25-30 for best results.", "Crop Planning"),
    AdvisorRecommendation("Pest Control Alert", "High humidity may lead to pest infestation. Consider preventive measures.", "Pest Management"),
    AdvisorRecommendation("Fertilizer Application", "Apply NPK fertilizer after 15 days of sowing for better growth.", "Nutrient Management")
)

private fun getGovernmentSchemes(): List<GovernmentScheme> = listOf(
    GovernmentScheme("PM-KISAN", "Direct income support of ₹6,000 per year to farmers", "All farmers", "₹6,000 annually"),
    GovernmentScheme("PMFBY", "Crop insurance scheme for farmers", "All farmers", "Insurance coverage"),
    GovernmentScheme("KCC", "Credit card for farmers with low interest rates", "All farmers", "Low interest loans")
)

// UI Components
@Composable
fun MarketPriceCard(price: MarketPrice) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = price.crop,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = price.market,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = price.price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = price.change,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (price.change.startsWith("+")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CommunityPostCard(post: CommunityPost) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.author,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = post.timeAgo,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Like",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${post.likes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Comment,
                        contentDescription = "Comment",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${post.comments}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileOptionCard(option: ProfileOption) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { option.onClick() },
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = stringResource(option.titleRes),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(option.titleRes),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = stringResource(R.string.navigate),
                tint = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CurrentWeatherCard(
    weatherViewModel: com.example.mittise.ui.weather.WeatherViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    
    // Check location permission on first launch
    androidx.compose.runtime.LaunchedEffect(Unit) {
        weatherViewModel.checkLocationPermission(context)
    }
    
    // Get location and weather data when permission is granted
    androidx.compose.runtime.LaunchedEffect(weatherViewModel.hasLocationPermission) {
        if (weatherViewModel.hasLocationPermission) {
            weatherViewModel.getCurrentLocation(context)
        }
    }
    
    // Refresh weather data when app becomes active
    androidx.compose.runtime.LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            if (weatherViewModel.hasLocationPermission && weatherViewModel.location != null) {
                weatherViewModel.refreshWeather()
            }
        }
    }
    
    EnhancedCard(
        gradientColors = GradientColors.primaryGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = "Weather",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = when {
                        weatherViewModel.isLoading -> "Loading..."
                        weatherViewModel.weatherData?.main?.temp != null -> 
                            "${weatherViewModel.weatherData!!.main!!.temp.toInt()}°C"
                        else -> "N/A"
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = when {
                        weatherViewModel.isLoading -> "Getting weather..."
                        weatherViewModel.weatherData?.weather?.isNotEmpty() == true -> 
                            weatherViewModel.weatherData!!.weather!![0].description.capitalize(Locale.current)
                        else -> "Weather unavailable"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                )
                Text(
                    text = when {
                        weatherViewModel.isLoading -> "Getting location..."
                        weatherViewModel.weatherData?.name != null -> 
                            weatherViewModel.weatherData!!.name!!
                        weatherViewModel.error != null -> "Location error"
                        else -> "Enable location"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

@Composable
fun WeatherForecastCard(forecast: WeatherForecast) {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = forecast.icon,
                contentDescription = forecast.condition,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = forecast.day,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = forecast.condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = forecast.temperature,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun CropActivityCard(activity: CropActivity) {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = activity.crop,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = activity.activity,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = activity.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (activity.status) {
                        "Completed" -> MaterialTheme.colorScheme.primary
                        "In Progress" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = activity.status,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SeasonalTipsCard() {
    EnhancedCard(
        gradientColors = GradientColors.successGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = "Tip",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Seasonal Tip",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "July is the perfect time to prepare your fields for the Kharif season. Ensure proper soil preparation and irrigation.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            )
        }
    }
}

@Composable
fun SoilTestingOptionCard(option: SoilTestingOption) {
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = option.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PreviousTestResultsCard() {
    EnhancedCard(
        gradientColors = GradientColors.tertiaryGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Previous Test Results",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "pH Level: 6.5 (Optimal)\nNitrogen: Medium\nPhosphorus: High\nPotassium: Low",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            )
        }
    }
}

@Composable
fun ArticleCard(article: Article) {
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "By ${article.author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = article.readTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AdvisorRecommendationCard(recommendation: AdvisorRecommendation) {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = recommendation.category,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recommendation.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GovernmentSchemeCard(scheme: GovernmentScheme) {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 6,
        cornerRadius = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = scheme.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = scheme.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Eligibility: ${scheme.eligibility}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Benefits: ${scheme.benefits}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
} 

// Data Classes for Chatbot
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

// Helper Functions for Chatbot
fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> "${diff / 86400000}d ago"
    }
}

fun simulateAIResponse(onResponse: (String) -> Unit) {
    // Simulate API delay
    GlobalScope.launch {
        delay(2000)
        val responses = listOf(
            "Based on current weather conditions, I recommend planting drought-resistant crops like millets and pulses.",
            "For pest control in tomatoes, you can use neem oil spray or introduce beneficial insects like ladybugs.",
            "The current market prices for wheat are ₹2,100 per quintal. Prices are expected to remain stable this week.",
            "For better soil health, consider crop rotation and adding organic matter like compost or green manure.",
            "The weather forecast shows light rain in the next 3 days. It's a good time for sowing seeds."
        )
        onResponse(responses.random())
    }
} 

@Composable
fun LiveWeatherForecastCard(forecast: com.example.mittise.data.model.ForecastItem) {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Weather icon based on weather condition
            val weatherIcon = when {
                forecast.weather.isNotEmpty() -> {
                    when {
                        forecast.weather[0].main.contains("Clear", ignoreCase = true) -> Icons.Filled.WbSunny
                        forecast.weather[0].main.contains("Cloud", ignoreCase = true) -> Icons.Filled.Cloud
                        forecast.weather[0].main.contains("Rain", ignoreCase = true) -> Icons.Filled.Opacity
                        forecast.weather[0].main.contains("Snow", ignoreCase = true) -> Icons.Filled.AcUnit
                        forecast.weather[0].main.contains("Thunder", ignoreCase = true) -> Icons.Filled.Thunderstorm
                        else -> Icons.Filled.WbSunny
                    }
                }
                else -> Icons.Filled.WbSunny
            }
            
            Icon(
                imageVector = weatherIcon,
                contentDescription = forecast.weather.firstOrNull()?.description ?: "Weather",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Format date
                val dateStr = try {
                    val date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        .parse(forecast.dtTxt.split(" ")[0])
                    val displayFormat = java.text.SimpleDateFormat("EEE, MMM dd", java.util.Locale.getDefault())
                    displayFormat.format(date!!)
                } catch (e: Exception) {
                    forecast.dtTxt.split(" ")[0]
                }
                
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = forecast.weather.firstOrNull()?.description?.capitalize(java.util.Locale.getDefault()) ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${forecast.main.temp.toInt()}°C",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Humidity: ${forecast.main.humidity}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 

@Composable
fun WeatherAnalysisCard(analysis: com.example.mittise.data.model.WeatherAnalysis) {
    val severityColor = when (analysis.severity) {
        "High" -> MaterialTheme.colorScheme.error
        "Medium" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    
    val severityIcon = when (analysis.severity) {
        "High" -> Icons.Filled.Warning
        "Medium" -> Icons.Filled.Info
        else -> Icons.Filled.CheckCircle
    }
    
    EnhancedCard(
        gradientColors = when (analysis.severity) {
            "High" -> GradientColors.warningGradient
            "Medium" -> GradientColors.tertiaryGradient
            else -> GradientColors.successGradient
        },
        elevation = 8,
        cornerRadius = 16
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = severityIcon,
                    contentDescription = "Severity",
                    tint = severityColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = analysis.condition,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Severity: ${analysis.severity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = severityColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Farming Advice
            if (analysis.farmingAdvice.isNotEmpty()) {
                AnalysisSection(
                    title = "Farming Advice",
                    icon = Icons.Filled.Agriculture,
                    items = analysis.farmingAdvice
                )
            }
            
            // Crop Recommendations
            if (analysis.cropRecommendations.isNotEmpty()) {
                AnalysisSection(
                    title = "Crop Recommendations",
                    icon = Icons.Filled.LocalFlorist,
                    items = analysis.cropRecommendations
                )
            }
            
            // Precautions
            if (analysis.precautions.isNotEmpty()) {
                AnalysisSection(
                    title = "Precautions",
                    icon = Icons.Filled.Security,
                    items = analysis.precautions
                )
            }
            
            // Optimal Activities
            if (analysis.optimalActivities.isNotEmpty()) {
                AnalysisSection(
                    title = "Optimal Activities",
                    icon = Icons.Filled.TrendingUp,
                    items = analysis.optimalActivities
                )
            }
        }
    }
}

@Composable
fun AnalysisSection(
    title: String,
    icon: ImageVector,
    items: List<String>
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        items.forEach { item ->
            Row(
                modifier = Modifier.padding(start = 28.dp, top = 4.dp)
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
} 