package com.example.mittise.ui.marketplace

import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.Product
import com.example.mittise.data.repository.MarketplaceRepository
import com.example.mittise.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarketplaceState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val selectedLocation: String? = null,
    val error: String? = null
)

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : BaseViewModel<MarketplaceState>() {

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
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    categories = categories,
                    locations = locations,
                    products = products
                )
            } catch (e: Exception) {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
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
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    products = products
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
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    products = products
                )
            } catch (e: Exception) {
                _marketplaceState.value = _marketplaceState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refresh() {
        loadInitialData()
    }
} 