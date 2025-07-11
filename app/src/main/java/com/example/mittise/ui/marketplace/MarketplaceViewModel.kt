package com.example.mittise.ui.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Sealed class to represent different types of marketplace items
sealed class MarketplaceItem {
    data class RegularProduct(val product: Product) : MarketplaceItem()
    data class FarmerProductItem(val product: com.example.mittise.data.model.FarmerProduct) : MarketplaceItem()
}

data class MarketplaceState(
    val isLoading: Boolean = false,
    val marketplaceItems: List<MarketplaceItem> = emptyList(),
    val categories: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val selectedLocation: String? = null,
    val showFarmerProducts: Boolean = true, // Toggle to show/hide farmer products
    val error: String? = null,
    val successMessage: String? = null
)

class MarketplaceViewModel : ViewModel() {

    private val initialCategories = listOf("Seeds", "Grains", "Fertilizers", "Pesticides", "Equipment", "Fruits", "Vegetables")
    private val initialLocations = listOf("Punjab", "Haryana", "Delhi", "Maharashtra", "Gujarat", "Karnataka", "Uttar Pradesh")
    private val initialProducts = listOf<Product>() // Optionally, add some fake products here
    private val initialFarmerProducts = listOf<com.example.mittise.data.model.FarmerProduct>() // Optionally, add some fake farmer products here

    private val _marketplaceState = MutableStateFlow(
        MarketplaceState(
            isLoading = false,
            marketplaceItems = createMarketplaceItems(initialProducts, initialFarmerProducts),
            categories = initialCategories,
            locations = initialLocations
        )
    )
    val marketplaceState: StateFlow<MarketplaceState> = _marketplaceState.asStateFlow()

    private fun createMarketplaceItems(
        products: List<Product>,
        farmerProducts: List<com.example.mittise.data.model.FarmerProduct>
    ): List<MarketplaceItem> {
        val items = mutableListOf<MarketplaceItem>()
        products.forEach { product ->
            items.add(MarketplaceItem.RegularProduct(product))
        }
        farmerProducts.forEach { farmerProduct ->
            items.add(MarketplaceItem.FarmerProductItem(farmerProduct))
        }
        return items
    }

    fun addFarmerProduct(farmerProduct: com.example.mittise.data.model.FarmerProduct) {
        viewModelScope.launch {
            val currentItems = _marketplaceState.value.marketplaceItems.toMutableList()
            currentItems.add(MarketplaceItem.FarmerProductItem(farmerProduct))
                _marketplaceState.value = _marketplaceState.value.copy(
                marketplaceItems = currentItems,
                successMessage = "Product added successfully!"
            )
            kotlinx.coroutines.delay(3000)
            _marketplaceState.value = _marketplaceState.value.copy(successMessage = null)
        }
    }

    fun selectCategory(category: String?) {
        _marketplaceState.value = _marketplaceState.value.copy(selectedCategory = category)
        // Optionally, filter items by category here if needed
    }

    fun selectLocation(location: String?) {
        _marketplaceState.value = _marketplaceState.value.copy(selectedLocation = location)
        // Optionally, filter items by location here if needed
    }

    fun toggleFarmerProducts() {
        val currentState = _marketplaceState.value
        _marketplaceState.value = currentState.copy(
            showFarmerProducts = !currentState.showFarmerProducts
        )
    }

    fun clearError() {
        _marketplaceState.value = _marketplaceState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _marketplaceState.value = _marketplaceState.value.copy(successMessage = null)
    }

    fun refresh() {
        // For local-only, just reset to initial state
        _marketplaceState.value = MarketplaceState(
            isLoading = false,
            marketplaceItems = createMarketplaceItems(initialProducts, initialFarmerProducts),
            categories = initialCategories,
            locations = initialLocations
        )
    }
} 