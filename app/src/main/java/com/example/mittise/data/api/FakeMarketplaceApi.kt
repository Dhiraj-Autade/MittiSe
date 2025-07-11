package com.example.mittise.data.api

import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product

class FakeMarketplaceApi : MarketplaceApi {
    override suspend fun getProducts(
        category: String?,
        location: String?,
        page: Int,
        limit: Int
    ): List<Product> {
        // Simulate network delay
        kotlinx.coroutines.delay(500)
        
        return listOf(
            Product(
                id = "1",
                name = "Premium Wheat Seeds",
                description = "High quality wheat seeds for better yield. Suitable for all soil types.",
                price = 250.0,
                unit = "kg",
                quantity = 100.0,
                imageUrl = "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=400",
                sellerId = "seller1",
                sellerName = "Ram Singh",
                location = "Punjab",
                category = "Seeds",
                rating = 4.5f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "2",
                name = "Organic Basmati Rice",
                description = "Premium organic basmati rice, naturally grown without chemicals.",
                price = 120.0,
                unit = "kg",
                quantity = 50.0,
                imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400",
                sellerId = "seller2",
                sellerName = "Shyam Kumar",
                location = "Haryana",
                category = "Grains",
                rating = 4.8f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "3",
                name = "NPK Fertilizer",
                description = "Balanced NPK fertilizer for all crops. Promotes healthy growth.",
                price = 850.0,
                unit = "bag",
                quantity = 25.0,
                imageUrl = "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400",
                sellerId = "seller3",
                sellerName = "Agro Solutions",
                location = "Delhi",
                category = "Fertilizers",
                rating = 4.2f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "4",
                name = "Tomato Seeds",
                description = "Hybrid tomato seeds for high yield. Disease resistant variety.",
                price = 180.0,
                unit = "packet",
                quantity = 200.0,
                imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?w=400",
                sellerId = "seller4",
                sellerName = "Green Farm",
                location = "Maharashtra",
                category = "Seeds",
                rating = 4.6f,
                isVerified = false,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "5",
                name = "Tractor Attachment",
                description = "Multi-purpose tractor attachment for various farming operations.",
                price = 15000.0,
                unit = "piece",
                quantity = 5.0,
                imageUrl = "https://images.unsplash.com/photo-1592982537447-7440770cbfc9?w=400",
                sellerId = "seller5",
                sellerName = "Farm Equipment Co.",
                location = "Gujarat",
                category = "Equipment",
                rating = 4.3f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "6",
                name = "Organic Pesticide",
                description = "Natural pesticide made from neem. Safe for crops and environment.",
                price = 450.0,
                unit = "liter",
                quantity = 30.0,
                imageUrl = "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400",
                sellerId = "seller6",
                sellerName = "Organic Solutions",
                location = "Karnataka",
                category = "Pesticides",
                rating = 4.7f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            )
        ).filter { product ->
            // Filter by category if specified
            category == null || product.category.equals(category, ignoreCase = true)
        }.filter { product ->
            // Filter by location if specified
            location == null || product.location.equals(location, ignoreCase = true)
        }
    }

    override suspend fun getFarmerProducts(
        category: String?,
        location: String?,
        page: Int,
        limit: Int
    ): List<FarmerProduct> {
        // Simulate network delay
        kotlinx.coroutines.delay(300)
        
        return listOf(
            FarmerProduct(
                id = "fp1",
                productName = "Fresh Mangoes",
                productType = "Fruits",
                quantity = 500.0,
                unit = "kg",
                expectedPrice = 80.0,
                priceUnit = "kg",
                location = "Maharashtra",
                farmerName = "Rajesh Patel",
                farmerContact = "+91 98765 43210",
                description = "Sweet and juicy Alphonso mangoes from our farm. Organic farming methods used.",
                imageUrl = "https://images.unsplash.com/photo-1553279768-865429fa0078?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            ),
            FarmerProduct(
                id = "fp2",
                productName = "Organic Tomatoes",
                productType = "Vegetables",
                quantity = 200.0,
                unit = "kg",
                expectedPrice = 40.0,
                priceUnit = "kg",
                location = "Karnataka",
                farmerName = "Lakshmi Devi",
                farmerContact = "+91 87654 32109",
                description = "Fresh organic tomatoes grown without pesticides. Perfect for cooking and salads.",
                imageUrl = "https://images.unsplash.com/photo-1546094096-0df4bcaaa337?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            ),
            FarmerProduct(
                id = "fp3",
                productName = "Basmati Rice",
                productType = "Grains",
                quantity = 1000.0,
                unit = "kg",
                expectedPrice = 120.0,
                priceUnit = "kg",
                location = "Punjab",
                farmerName = "Gurpreet Singh",
                farmerContact = "+91 76543 21098",
                description = "Premium quality basmati rice from our family farm. Traditional farming methods.",
                imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            ),
            FarmerProduct(
                id = "fp4",
                productName = "Fresh Onions",
                productType = "Vegetables",
                quantity = 300.0,
                unit = "kg",
                expectedPrice = 25.0,
                priceUnit = "kg",
                location = "Gujarat",
                farmerName = "Ahmed Khan",
                farmerContact = "+91 65432 10987",
                description = "Large, fresh onions from our farm. Good for storage and daily use.",
                imageUrl = "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            ),
            FarmerProduct(
                id = "fp5",
                productName = "Sweet Corn",
                productType = "Vegetables",
                quantity = 150.0,
                unit = "kg",
                expectedPrice = 60.0,
                priceUnit = "kg",
                location = "Haryana",
                farmerName = "Sita Ram",
                farmerContact = "+91 54321 09876",
                description = "Sweet and tender corn cobs. Perfect for boiling or grilling.",
                imageUrl = "https://images.unsplash.com/photo-1601593768797-9acb5d1a0c9a?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            ),
            FarmerProduct(
                id = "fp6",
                productName = "Fresh Potatoes",
                productType = "Vegetables",
                quantity = 400.0,
                unit = "kg",
                expectedPrice = 30.0,
                priceUnit = "kg",
                location = "Uttar Pradesh",
                farmerName = "Ram Kumar",
                farmerContact = "+91 43210 98765",
                description = "Fresh potatoes from our farm. Good quality for cooking and storage.",
                imageUrl = "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=400",
                isAvailable = true,
                createdAt = System.currentTimeMillis()
            )
        ).filter { product ->
            // Filter by category if specified
            category == null || product.productType.equals(category, ignoreCase = true)
        }.filter { product ->
            // Filter by location if specified
            location == null || product.location.equals(location, ignoreCase = true)
        }
    }

    override suspend fun getProductDetails(productId: String): Product {
        return getProducts(null, null, 1, 20).find { it.id == productId } ?: getProducts(null, null, 1, 20).first()
    }

    override suspend fun getFarmerProductDetails(productId: String): FarmerProduct {
        return getFarmerProducts(null, null, 1, 20).find { it.id == productId } ?: getFarmerProducts(null, null, 1, 20).first()
    }

    override suspend fun getCategories(): List<String> {
        return listOf("Seeds", "Grains", "Fertilizers", "Pesticides", "Equipment", "Fruits", "Vegetables")
    }

    override suspend fun getLocations(): List<String> {
        return listOf("Punjab", "Haryana", "Delhi", "Maharashtra", "Gujarat", "Karnataka", "Uttar Pradesh")
    }
} 