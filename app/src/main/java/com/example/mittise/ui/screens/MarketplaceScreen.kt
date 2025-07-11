package com.example.mittise.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    onProductClick: (FarmerProduct) -> Unit,
    onRegisterProduct: () -> Unit
) {
    val viewModel: MarketplaceViewModel = viewModel()
    val marketplaceState by viewModel.marketplaceState.collectAsState()
    var showRegistrationForm by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showLocationDropdown by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    // Filter products based on search query
    val filteredItems = marketplaceState.marketplaceItems.filter { item ->
        when (item) {
            is MarketplaceItem.RegularProduct -> {
                item.product.name.contains(searchQuery, ignoreCase = true) ||
                item.product.location.contains(searchQuery, ignoreCase = true)
            }
            is MarketplaceItem.FarmerProductItem -> {
                item.product.productName.contains(searchQuery, ignoreCase = true) ||
                item.product.location.contains(searchQuery, ignoreCase = true) ||
                item.product.productType.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Product Count
            item {
                EnhancedCard(
                    gradientColors = GradientColors.primaryGradient,
                    elevation = 12,
                    cornerRadius = 20
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                        Text(
                            text = stringResource(R.string.marketplace_title),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                                    text = "Showing ${filteredItems.size} products",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search products, locations...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Filters
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Filter
                    ExposedDropdownMenuBox(
                        expanded = showCategoryDropdown,
                        onExpandedChange = { showCategoryDropdown = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = marketplaceState.selectedCategory ?: "All Categories",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
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
                        ExposedDropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            listOf("All Categories", "Vegetables", "Fruits", "Grains", "Dairy", "Poultry").forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        viewModel.selectCategory(if (category == "All Categories") null else category)
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Location Filter
                    ExposedDropdownMenuBox(
                        expanded = showLocationDropdown,
                        onExpandedChange = { showLocationDropdown = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = marketplaceState.selectedLocation ?: "All Locations",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Location") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showLocationDropdown) },
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
                        ExposedDropdownMenu(
                            expanded = showLocationDropdown,
                            onDismissRequest = { showLocationDropdown = false }
                        ) {
                            listOf("All Locations", "Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata").forEach { location ->
                                DropdownMenuItem(
                                    text = { Text(location) },
                                    onClick = {
                                        viewModel.selectLocation(if (location == "All Locations") null else location)
                                        showLocationDropdown = false
                                    }
                                )
                            }
                        }
                    }
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
                            TextButton(
                                onClick = { viewModel.clearError() }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }

            // Success Message
            marketplaceState.successMessage?.let { message ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { viewModel.clearSuccessMessage() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Products List
            if (filteredItems.isEmpty() && !marketplaceState.isLoading) {
                item {
                    EmptyStateCard()
                }
            } else {
                items(filteredItems) { item ->
                    when (item) {
                        is MarketplaceItem.RegularProduct -> {
                            RegularProductCard(
                                product = item.product,
                                onClick = { /* Handle regular product click */ }
                            )
                        }
                        is MarketplaceItem.FarmerProductItem -> {
                            EnhancedFarmerProductCard(
                                product = item.product,
                                onClick = { onProductClick(item.product) }
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showRegistrationForm = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Product",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Modal Bottom Sheet for Product Registration
    if (showRegistrationForm) {
        ModalBottomSheet(
            onDismissRequest = { showRegistrationForm = false },
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            EnhancedProductRegistrationForm(
                onRegister = { product ->
                    viewModel.addFarmerProduct(product)
                    scope.launch {
                        bottomSheetState.hide()
                        showRegistrationForm = false
                    }
                },
                onCancel = {
                    scope.launch {
                        bottomSheetState.hide()
                        showRegistrationForm = false
                    }
                }
            )
        }
    }
}

@Composable
fun EnhancedFarmerProductCard(
    product: FarmerProduct,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    EnhancedCard(
        modifier = Modifier.clickable { onClick() },
        gradientColors = GradientColors.cardGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with Product Image and Basic Info
        Row(
                verticalAlignment = Alignment.Top
        ) {
            // Product Image
            Card(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = if (product.imageUrl != null && product.imageUrl.startsWith("content://")) {
                        product.imageUrl
                    } else {
                        R.drawable.default_profile
                    },
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
                        color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Category Tag
                    GradientChip(
                        text = product.productType,
                        onClick = { },
                        modifier = Modifier.padding(top = 4.dp),
                        gradientColors = GradientColors.successGradient
                )
                
                    // Price and Quantity
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = "₹${product.expectedPrice}",
                            style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                            )
                    )
                    Text(
                            text = " / ${product.priceUnit}",
                            style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Available: ${product.quantity} ${product.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Location and Farmer Info
                Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                    
                Row(
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Timestamp
                Text(
                text = "Listed on ${dateFormat.format(Date(product.createdAt))}",
                    style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Handle contact */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
        ) {
            Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Contact",
                        modifier = Modifier.size(16.dp)
            )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Contact")
                }
                
                Button(
                    onClick = { /* Handle view details */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View Details",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Details")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProductRegistrationForm(
    onRegister: (FarmerProduct) -> Unit,
    onCancel: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
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
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var showProductTypeDropdown by remember { mutableStateOf(false) }
    var showUnitDropdown by remember { mutableStateOf(false) }
    var showPriceUnitDropdown by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    
    val productTypes = listOf("Vegetables", "Fruits", "Grains", "Pulses", "Spices", "Dairy", "Poultry", "Other")
    val units = listOf("kg", "ton", "quintal", "dozen", "piece", "liters")
    val priceUnits = listOf("per kg", "per ton", "per quintal", "per dozen", "per piece", "per liter")
    
    // Photo picker launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            selectedImageUri = null
        }
    }
    
    // Helper function to create temporary file for camera
    fun createTempImageFile(): Uri {
        val tempFile = File.createTempFile("product_image_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
        return Uri.fromFile(tempFile)
    }
    
    // Handle camera permission if needed
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            selectedImageUri = createTempImageFile()
            cameraLauncher.launch(selectedImageUri)
        }
    }
    
    // Form validation
    val isFormValid = productName.isNotEmpty() && 
                     productType.isNotEmpty() && 
                     quantity.isNotEmpty() && 
                     unit.isNotEmpty() && 
                     expectedPrice.isNotEmpty() && 
                     priceUnit.isNotEmpty() &&
                     farmerName.isNotEmpty() && 
                     farmerContact.isNotEmpty() && 
                     location.isNotEmpty()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = "Register Your Product",
                style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
        )
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Success Message
        if (showSuccessMessage) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Product added successfully!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        
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
                Box {
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { showImageSourceDialog = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = R.drawable.default_profile,
                                contentDescription = "Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    
                    // Image selected indicator
                    if (selectedImageUri != null) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Image Selected",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedImageUri == null) {
                    Text(
                        text = "Tap to add a product photo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showImageSourceDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (selectedImageUri == null) "Add Photo" else "Change Photo")
                    }
                    if (selectedImageUri != null) {
                        OutlinedButton(
                            onClick = { selectedImageUri = null }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Photo",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Remove")
                        }
                    }
                }
            }
        }
        
        // Image Source Dialog
        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Select Image Source") },
                text = { Text("Choose how you want to add a product photo") },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showImageSourceDialog = false
                                galleryLauncher.launch("image/*")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Gallery",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Gallery")
                        }
                        Button(
                            onClick = {
                                showImageSourceDialog = false
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Camera")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImageSourceDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        
        // Product Name
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name *") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // Product Type Dropdown
        ExposedDropdownMenuBox(
            expanded = showProductTypeDropdown,
            onExpandedChange = { showProductTypeDropdown = it }
        ) {
            OutlinedTextField(
                value = productType,
                onValueChange = { },
                readOnly = true,
                label = { Text("Product Type *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showProductTypeDropdown) },
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
            ExposedDropdownMenu(
                expanded = showProductTypeDropdown,
                onDismissRequest = { showProductTypeDropdown = false }
            ) {
                productTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            productType = type
                            showProductTypeDropdown = false
                        }
                    )
                    }
                }
        }
        
        // Quantity and Unit Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity *") },
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
                expanded = showUnitDropdown,
                onExpandedChange = { showUnitDropdown = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = unit,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Unit *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showUnitDropdown) },
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
                ExposedDropdownMenu(
                    expanded = showUnitDropdown,
                    onDismissRequest = { showUnitDropdown = false }
                ) {
                    units.forEach { unitOption ->
                        DropdownMenuItem(
                            text = { Text(unitOption) },
                            onClick = {
                                unit = unitOption
                                showUnitDropdown = false
                            }
                        )
                    }
                }
            }
        }
        
        // Price and Price Unit Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = expectedPrice,
                onValueChange = { expectedPrice = it },
                label = { Text("Price *") },
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
                expanded = showPriceUnitDropdown,
                onExpandedChange = { showPriceUnitDropdown = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = priceUnit,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Price Unit *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriceUnitDropdown) },
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
                ExposedDropdownMenu(
                    expanded = showPriceUnitDropdown,
                    onDismissRequest = { showPriceUnitDropdown = false }
                ) {
                    priceUnits.forEach { priceUnitOption ->
                        DropdownMenuItem(
                            text = { Text(priceUnitOption) },
                            onClick = {
                                priceUnit = priceUnitOption
                                showPriceUnitDropdown = false
                            }
                        )
                    }
                }
            }
        }
        
        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
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
        
        // Farmer Name
        OutlinedTextField(
            value = farmerName,
            onValueChange = { farmerName = it },
            label = { Text("Farmer Name *") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // Farmer Contact
        OutlinedTextField(
            value = farmerContact,
            onValueChange = { farmerContact = it },
            label = { Text("Contact Number *") },
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
        
        // Location
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Farm Location *") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = if (isDark) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = if (isDark) Color.White else MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Cancel")
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
                        imageUrl = selectedImageUri?.toString()
                    )
                    onRegister(product)
                    showSuccessMessage = true
                },
                modifier = Modifier.weight(1f),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Register Product")
            }
        }
        
        // Required fields note
        Text(
            text = "* Required fields",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
} 