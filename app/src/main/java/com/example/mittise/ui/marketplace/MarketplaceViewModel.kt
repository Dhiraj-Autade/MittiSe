package com.example.mittise.ui.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    // Store all products for filtering
    private var initialFarmerProducts: List<FarmerProduct> = emptyList()

    private fun filterMarketplaceItemsByCategory() {
        val selectedCategory = _marketplaceState.value.selectedCategory
        val allProducts = initialFarmerProducts
        val filteredProducts = if (selectedCategory == null || selectedCategory == "All Categories") {
            allProducts
        } else {
            allProducts.filter { it.productType == selectedCategory }
        }
        _marketplaceState.value = _marketplaceState.value.copy(
            marketplaceItems = createMarketplaceItems(initialProducts, filteredProducts)
        )
    }

    private val initialCategories = listOf("Seeds", "Grains", "Fertilizers", "Pesticides", "Equipment", "Fruits", "Vegetables")
    private val initialLocations = listOf("Punjab", "Haryana", "Delhi", "Maharashtra", "Gujarat", "Karnataka", "Uttar Pradesh")
    private val initialProducts = listOf<Product>()

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
        farmerProducts: List<FarmerProduct>
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

    init {
        observeCurrentUserProducts()
    }

    fun addFarmerProduct(farmerProduct: FarmerProduct) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _marketplaceState.value = _marketplaceState.value.copy(
                error = "Please sign in to list a product."
            )
            return
        }

        // Generate unique product ID if not present
        val productId = if (farmerProduct.id.isNotBlank()) farmerProduct.id else java.util.UUID.randomUUID().toString()

        val data = mapOf(
            "productName" to farmerProduct.productName,
            "productType" to farmerProduct.productType,
            "quantity" to farmerProduct.quantity,
            "unit" to farmerProduct.unit,
            "expectedPrice" to farmerProduct.expectedPrice,
            "priceUnit" to farmerProduct.priceUnit,
            "location" to farmerProduct.location,
            "farmerName" to farmerProduct.farmerName,
            "farmerContact" to farmerProduct.farmerContact,
            "description" to farmerProduct.description,
            "imageUrl" to farmerProduct.imageUrl,
            "isAvailable" to farmerProduct.isAvailable,
            "createdAt" to System.currentTimeMillis(),
            "userId" to userId,
            "id" to productId
        )

        firestore
            .collection("users")
            .document(userId)
            .collection("marketplace_products")
            .document(productId)
            .set(data)
            .addOnSuccessListener {
                _marketplaceState.value = _marketplaceState.value.copy(
                    successMessage = "Product added successfully!"
                )
            }
            .addOnFailureListener { e ->
                _marketplaceState.value = _marketplaceState.value.copy(
                    error = e.message ?: "Failed to add product"
                )
            }
    }

    fun selectCategory(category: String?) {
    _marketplaceState.value = _marketplaceState.value.copy(selectedCategory = category)
    filterMarketplaceItemsByCategory()
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
        observeCurrentUserProducts()
    }

    private fun observeCurrentUserProducts() {
        val userId = auth.currentUser?.uid
        if (userId == null) return

        _marketplaceState.value = _marketplaceState.value.copy(isLoading = true)

        firestore
            .collection("users")
            .document(userId)
            .collection("marketplace_products")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _marketplaceState.value = _marketplaceState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    return@addSnapshotListener
                }

                val farmerProducts = snapshot?.documents?.map { doc ->
                    mapDocumentToFarmerProduct(doc)
                } ?: emptyList()

                // Store all products for filtering
                initialFarmerProducts = farmerProducts
                filterMarketplaceItemsByCategory()
                _marketplaceState.value = _marketplaceState.value.copy(isLoading = false)
            }
    // Store all products for filtering
    var initialFarmerProducts: List<FarmerProduct> = emptyList()

    fun filterMarketplaceItemsByCategory() {
        val selectedCategory = _marketplaceState.value.selectedCategory
        val allProducts = initialFarmerProducts
        val filteredProducts = if (selectedCategory == null || selectedCategory == "All Categories") {
            allProducts
        } else {
            allProducts.filter { it.productType == selectedCategory }
        }
        _marketplaceState.value = _marketplaceState.value.copy(
            marketplaceItems = createMarketplaceItems(initialProducts, filteredProducts)
        )
    }
    }

    private fun mapDocumentToFarmerProduct(doc: DocumentSnapshot): FarmerProduct {
        return FarmerProduct(
            id = doc.id,
            productName = doc.getString("productName") ?: "",
            productType = doc.getString("productType") ?: "",
            quantity = (doc.getDouble("quantity") ?: 0.0),
            unit = doc.getString("unit") ?: "kg",
            expectedPrice = (doc.getDouble("expectedPrice") ?: 0.0),
            priceUnit = doc.getString("priceUnit") ?: "per kg",
            location = doc.getString("location") ?: "",
            farmerName = doc.getString("farmerName") ?: "",
            farmerContact = doc.getString("farmerContact") ?: "",
            description = doc.getString("description") ?: "",
            imageUrl = doc.getString("imageUrl"),
            isAvailable = doc.getBoolean("isAvailable") ?: true,
            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
        )
    }

    fun deleteFarmerProduct(product: FarmerProduct) {
        val userId = auth.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .collection("marketplace_products")
            .document(product.id)
            .delete()
            .addOnFailureListener { e ->
                _marketplaceState.value = _marketplaceState.value.copy(
                    error = e.message ?: "Failed to delete product"
                )
            }
    }
} 