package com.example.mittise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mittise.R
import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product
import com.example.mittise.ui.marketplace.MarketplaceViewModel
import com.example.mittise.ui.marketplace.MarketplaceItem
import com.example.mittise.ui.theme.*
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    onProductClick: (FarmerProduct) -> Unit,
    onRegisterProduct: () -> Unit
) {
    val viewModel: MarketplaceViewModel = viewModel()
    val marketplaceState by viewModel.marketplaceState.collectAsState()
    var showRegistrationForm by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    if (showRegistrationForm) {
        ProductRegistrationForm(
            onRegister = { product ->
                // TODO: Add product to repository
                showRegistrationForm = false
            },
            onCancel = {
                showRegistrationForm = false
            }
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                            text = stringResource(R.string.marketplace_title),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            text = "Buy and sell agricultural products",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Filters
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Filter
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = marketplaceState.selectedCategory ?: "All Categories",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    // Location Filter
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = marketplaceState.selectedLocation ?: "All Locations",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Location") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Register Product Button
            item {
                Button(
                    onClick = { showRegistrationForm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Product",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Register Your Product")
                }
            }

            // Loading State
            if (marketplaceState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Error State
            marketplaceState.error?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Error loading products",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Products List
            if (marketplaceState.marketplaceItems.isEmpty() && !marketplaceState.isLoading) {
                item {
                    EmptyStateCard()
                }
            } else {
                items(marketplaceState.marketplaceItems) { item ->
                    when (item) {
                        is MarketplaceItem.RegularProduct -> {
                            RegularProductCard(
                                product = item.product,
                                onClick = { /* Handle regular product click */ }
                            )
                        }
                        is MarketplaceItem.FarmerProduct -> {
                            FarmerProductCard(
                                product = item.product,
                                onClick = { onProductClick(item.product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegularProductCard(
    product: Product,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        modifier = Modifier.clickable { onClick() },
        gradientColors = GradientColors.cardGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.default_profile)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " / ${product.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Available: ${product.quantity} ${product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Seller",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.sellerName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Rating
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = product.rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    EnhancedCard(
        gradientColors = GradientColors.cardGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "No Products",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No products available",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Be the first to register your product!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun FarmerProductCard(
    product: FarmerProduct,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    EnhancedCard(
        modifier = Modifier.clickable { onClick() },
        gradientColors = GradientColors.cardGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.default_profile)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = product.productType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.expectedPrice}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " ${product.priceUnit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Available: ${product.quantity} ${product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Farmer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.farmerName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Contact Button
            IconButton(
                onClick = { /* Handle contact */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Contact Farmer",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRegistrationForm(
    onRegister: (FarmerProduct) -> Unit,
    onCancel: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var productName by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var expectedPrice by remember { mutableStateOf("") }
    var priceUnit by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var farmerName by remember { mutableStateOf("") }
    var farmerContact by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    
    val productTypes = listOf("Fruit", "Vegetable", "Grain", "Pulse", "Spice", "Other")
    val units = listOf("kg", "ton", "quintal", "dozen", "piece")
    val priceUnits = listOf("per kg", "per ton", "per quintal", "per dozen", "per piece")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.farmer_product_registration_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
        )
        
        // Product Image Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AsyncImage(
                        model = R.drawable.default_profile,
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Camera */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Camera")
                    }
                    OutlinedButton(
                        onClick = { /* Gallery */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gallery")
                    }
                }
            }
        }
        
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text(stringResource(R.string.farmer_product_name)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { },
        ) {
            OutlinedTextField(
                value = productType,
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.product_type)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text(stringResource(R.string.farmer_product_quantity)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { },
            ) {
                OutlinedTextField(
                    value = unit,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.unit)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = expectedPrice,
                onValueChange = { expectedPrice = it },
                label = { Text(stringResource(R.string.farmer_product_price)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { },
            ) {
                OutlinedTextField(
                    value = priceUnit,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.price_unit)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.farmer_product_description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        OutlinedTextField(
            value = farmerName,
            onValueChange = { farmerName = it },
            label = { Text(stringResource(R.string.farmer_name)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        OutlinedTextField(
            value = farmerContact,
            onValueChange = { farmerContact = it },
            label = { Text(stringResource(R.string.farmer_product_contact)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(stringResource(R.string.farmer_product_location)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Cancel", color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface)
            }
            Button(
                onClick = {
                    val product = FarmerProduct(
                        id = System.currentTimeMillis().toString(),
                        productName = productName,
                        productType = productType,
                        quantity = quantity.toDoubleOrNull() ?: 0.0,
                        unit = unit,
                        expectedPrice = expectedPrice.toDoubleOrNull() ?: 0.0,
                        priceUnit = priceUnit,
                        location = location,
                        farmerName = farmerName,
                        farmerContact = farmerContact,
                        description = description,
                        imageUrl = null
                    )
                    onRegister(product)
                },
                modifier = Modifier.weight(1f),
                enabled = productName.isNotEmpty() && productType.isNotEmpty() && 
                         quantity.isNotEmpty() && unit.isNotEmpty() && 
                         expectedPrice.isNotEmpty() && priceUnit.isNotEmpty() &&
                         farmerName.isNotEmpty() && farmerContact.isNotEmpty() && 
                         location.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(stringResource(R.string.farmer_product_register), color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface)
            }
        }
    }
} 