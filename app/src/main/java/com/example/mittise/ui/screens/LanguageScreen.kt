package com.example.mittise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mittise.R
import com.example.mittise.ui.language.LanguageViewModel
import com.example.mittise.ui.theme.*
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun LanguageScreen() {
    val viewModel: LanguageViewModel = viewModel()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val supportedLanguages by viewModel.supportedLanguages.collectAsState()
    var isChangingLanguage by remember { mutableStateOf(false) }
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
            // Header
            EnhancedCard(
                gradientColors = GradientColors.primaryGradient,
                elevation = 12,
                cornerRadius = 20
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.language_title),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = stringResource(R.string.language_choose_hint),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                                ),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        items(supportedLanguages.toList()) { (languageCode, languageName) ->
            EnhancedLanguageOptionCard(
                languageCode = languageCode,
                languageName = languageName,
                isSelected = currentLanguage == languageCode,
                isCurrentLanguage = currentLanguage == languageCode,
                onClick = {
                    if (languageCode != currentLanguage && !isChangingLanguage) {
                        isChangingLanguage = true
                        viewModel.setLanguage(languageCode)
                    }
                }
            )
        }

        item {
            EnhancedCard(
                gradientColors = GradientColors.secondaryGradient,
                elevation = 8,
                cornerRadius = 16
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
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
                            text = stringResource(R.string.language_change_title),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isChangingLanguage) {
                            stringResource(R.string.language_changing_message)
                        } else {
                            stringResource(R.string.language_tap_message)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedLanguageOptionCard(
    languageCode: String,
    languageName: String,
    isSelected: Boolean,
    isCurrentLanguage: Boolean,
    onClick: () -> Unit
) {
    val gradientColors = when {
        isSelected -> GradientColors.primaryGradient
        isCurrentLanguage -> GradientColors.successGradient
        else -> GradientColors.tertiaryGradient
    }
    
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        gradientColors = gradientColors,
        elevation = if (isSelected) 12 else 6,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language Icon
            Icon(
                imageVector = getLanguageIcon(languageCode),
                contentDescription = languageName,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Language Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = languageName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                
                if (isCurrentLanguage) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Status Indicator
                        StatusIndicator(
                            isOnline = true,
                            modifier = Modifier.size(8.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(6.dp))
                        
                        Text(
                            text = stringResource(R.string.current_language),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
            
            // Selection Indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun getLanguageIcon(languageCode: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (languageCode) {
        "en" -> Icons.Default.Language
        "hi" -> Icons.Default.Language
        "mr" -> Icons.Default.Language
        "ta" -> Icons.Default.Language
        "te" -> Icons.Default.Language
        "ml" -> Icons.Default.Language
        else -> Icons.Default.Language
    }
} 