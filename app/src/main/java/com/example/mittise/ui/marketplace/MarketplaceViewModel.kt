package com.example.mittise.ui.marketplace

import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product
import com.example.mittise.data.repository.MarketplaceRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Sealed class to represent different types of marketplace items
sealed class MarketplaceItem {
    data class RegularProduct(val product: Product) : MarketplaceItem()
    data class FarmerProduct(val product: com.example.mittise.data.model.FarmerProduct) : MarketplaceItem()
}

data class MarketplaceState(
    val isLoading: Boolean = false,
    val marketplaceItems: List<MarketplaceItem> = emptyList(),
    val categories: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val selectedLocation: String? = null,
    val showFarmerProducts: Boolean = true, // Toggle to show/hide farmer products
    val error: String? = null
)

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _marketplaceState = MutableStateFlow(MarketplaceState())
    val marketplaceState: StateFlow<MarketplaceState> = _marketplaceState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _marketplaceState.value = _marketplaceState.value.copy(isLoading = true)
                val categories = repository.getCategories()
                val locations = repository.getLocations()
                val products = repository.getProducts()
                val farmerProducts = repository.getFarmerProducts()
                val marketplaceItems = createMarketplaceItems(products, farmerProducts)
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    categories = categories,
                    locations = locations,
                    marketplaceItems = marketplaceItems
                )
            } catch (e: Exception) {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun createMarketplaceItems(
        products: List<Product>,
        farmerProducts: List<com.example.mittise.data.model.FarmerProduct>
    ): List<MarketplaceItem> {
        val items = mutableListOf<MarketplaceItem>()
        products.forEach { product ->
            items.add(MarketplaceItem.RegularProduct(product))
        }
        farmerProducts.forEach { farmerProduct ->
            items.add(MarketplaceItem.FarmerProduct(farmerProduct))
        }
        return items
    }

    fun selectCategory(category: String?) {
        viewModelScope.launch {
            try {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = true,
                    selectedCategory = category
                )
                val products = repository.getProducts(
                    category = category,
                    location = _marketplaceState.value.selectedLocation
                )
                val farmerProducts = repository.getFarmerProducts(
                    category = category,
                    location = _marketplaceState.value.selectedLocation
                )
                val marketplaceItems = createMarketplaceItems(products, farmerProducts)
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    marketplaceItems = marketplaceItems
                )
            } catch (e: Exception) {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun selectLocation(location: String?) {
        viewModelScope.launch {
            try {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = true,
                    selectedLocation = location
                )
                val products = repository.getProducts(
                    category = _marketplaceState.value.selectedCategory,
                    location = location
                )
                val farmerProducts = repository.getFarmerProducts(
                    category = _marketplaceState.value.selectedCategory,
                    location = location
                )
                val marketplaceItems = createMarketplaceItems(products, farmerProducts)
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    marketplaceItems = marketplaceItems
                )
            } catch (e: Exception) {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun toggleFarmerProducts() {
        val currentState = _marketplaceState.value
        _marketplaceState.value = currentState.copy(
            showFarmerProducts = !currentState.showFarmerProducts
        )
    }

    fun refresh() {
        loadInitialData()
    }
} 