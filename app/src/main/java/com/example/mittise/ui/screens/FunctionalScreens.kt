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
import com.example.mittise.R
import com.example.mittise.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mittise.ui.viewmodels.ChatbotViewModel

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
                            imageVector = Icons.Default.Info,
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
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    
    var messageText by remember { mutableStateOf("") }
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    
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
                    verticalAlignment = Alignment.CenterVertically
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
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "AI Assistant",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
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
                }
            },
            onVoiceClick = { viewModel.toggleVoiceInput() },
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
                text = "👋 Hello! I'm your AI farming assistant",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "I can help you with:\n• Crop recommendations\n• Weather advice\n• Pest control tips\n• Market prices\n• Farming techniques\n• And much more!",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color.Black.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Try asking me something like:\n\"What crops are best for this season?\"\n\"How to control pests in tomatoes?\"\n\"What's the weather forecast?\"",
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
                                imageVector = Icons.Default.VolumeUp,
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
                    imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicNone,
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
                    imageVector = Icons.Default.Send,
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
fun ProfileScreen() {
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
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Farmer Name",
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
                }
            }
        }

        // Profile Actions
        item {
            Button(
                onClick = { /* Navigate to edit profile */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }

        // Profile Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatCard(
                    title = "Posts",
                    value = "12",
                    icon = Icons.Default.Article
                )
                ProfileStatCard(
                    title = "Followers",
                    value = "45",
                    icon = Icons.Default.People
                )
                ProfileStatCard(
                    title = "Following",
                    value = "23",
                    icon = Icons.Default.People
                )
            }
        }

        // Profile Options
        items(getProfileOptions()) { option ->
            ProfileOptionCard(option = option)
        }
    }
}

// Weather Screen
@Composable
fun WeatherScreen() {
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
            CurrentWeatherCard()
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

        items(getWeatherForecast()) { forecast ->
            WeatherForecastCard(forecast = forecast)
        }

        // Weather Alerts
        item {
            WeatherAlertsCard()
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
                        text = "Crop Calendar",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Plan your farming activities with seasonal guidance",
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
                text = "This Month's Activities",
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
                        text = "Soil Testing",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Analyze your soil for better crop planning",
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
                    imageVector = Icons.Default.Science,
                    contentDescription = "Test Soil",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Test Your Soil")
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
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Ask Question",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ask AI Advisor")
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
    val title: String,
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

private fun getProfileOptions(): List<ProfileOption> = listOf(
    ProfileOption("Settings", Icons.Default.Settings) { },
    ProfileOption("Notifications", Icons.Default.Notifications) { },
    ProfileOption("Privacy", Icons.Default.Security) { },
    ProfileOption("Help & Support", Icons.Default.Help) { }
)

private fun getWeatherForecast(): List<WeatherForecast> = listOf(
    WeatherForecast("Today", "28°C", "Sunny", Icons.Default.WbSunny),
    WeatherForecast("Tomorrow", "26°C", "Partly Cloudy", Icons.Default.Cloud),
    WeatherForecast("Wed", "24°C", "Rain", Icons.Default.Opacity),
    WeatherForecast("Thu", "27°C", "Sunny", Icons.Default.WbSunny),
    WeatherForecast("Fri", "29°C", "Sunny", Icons.Default.WbSunny)
)

private fun getCropCalendarActivities(): List<CropActivity> = listOf(
    CropActivity("Wheat", "Harvesting", "15-20 July", "In Progress"),
    CropActivity("Rice", "Sowing", "25-30 July", "Upcoming"),
    CropActivity("Cotton", "Pest Control", "10-15 July", "Completed")
)

private fun getSoilTestingOptions(): List<SoilTestingOption> = listOf(
    SoilTestingOption("pH Testing", "Check soil acidity/alkalinity", Icons.Default.Science),
    SoilTestingOption("Nutrient Analysis", "Test NPK levels", Icons.Default.Science),
    SoilTestingOption("Organic Matter", "Check soil organic content", Icons.Default.Science)
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
                        imageVector = Icons.Default.Favorite,
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
                        imageVector = Icons.Default.Comment,
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
                contentDescription = option.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = if (isDark) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CurrentWeatherCard() {
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
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Weather",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "28°C",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Sunny",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                )
                Text(
                    text = "Mumbai, Maharashtra",
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
fun WeatherAlertsCard() {
    EnhancedCard(
        gradientColors = GradientColors.warningGradient,
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
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alert",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Weather Alert",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Heavy rainfall expected in the next 48 hours. Plan your farming activities accordingly.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
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
                    imageVector = Icons.Default.Lightbulb,
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
                    imageVector = Icons.Default.History,
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