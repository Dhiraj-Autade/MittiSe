package com.example.mittise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mittise.R
import com.example.mittise.data.model.Product
import com.example.mittise.data.model.FarmerProduct
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import com.example.mittise.ui.theme.*
import androidx.compose.ui.graphics.vector.ImageVector

object PlaceholderScreens {
    // Only keep the following composables:
    @Composable
    fun ProductDetailsScreen(productId: String) {
        createPlaceholderScreen(
            title = "Product Details",
            icon = Icons.Default.ShoppingCart,
            description = "Product details for ID: $productId coming soon"
        )
    }

    @Composable
    fun PostDetailsScreen(postId: String) {
        createPlaceholderScreen(
            title = "Post Details",
            icon = Icons.Default.Article,
            description = "Post details for ID: $postId coming soon"
        )
    }

    @Composable
    fun EditProfileScreen() {
        createPlaceholderScreen(
            title = "Edit Profile",
            icon = Icons.Default.Person,
            description = "Profile editing features coming soon"
        )
    }

    @Composable
    fun FarmerProductRegistrationScreen() {
        createPlaceholderScreen(
            title = "Register Product",
            icon = Icons.Default.Add,
            description = "Farmer product registration features coming soon"
        )
    }

    @Composable
    private fun createPlaceholderScreen(
        title: String,
        description: String,
        icon: ImageVector
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Title
            GradientText(
                text = title,
                gradientColors = GradientColors.primaryGradient,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Coming Soon Badge
            GradientChip(
                text = "Coming Soon",
                onClick = { /* No action */ },
                gradientColors = GradientColors.secondaryGradient
            )
        }
    }
} 