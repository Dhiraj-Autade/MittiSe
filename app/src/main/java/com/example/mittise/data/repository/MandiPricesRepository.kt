package com.example.mittise.data.repository

import android.util.Log
import com.example.mittise.data.api.MandiPricesApi
import com.example.mittise.data.model.MandiPrice
import com.example.mittise.data.model.MandiApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MandiPricesRepository(
    private val mandiPricesApi: MandiPricesApi
) {
    companion object {
        private const val TAG = "MandiPricesRepository"
        
        // All 28 Indian states and union territories
        private val ALL_INDIAN_STATES = listOf(
            "Andhra Pradesh",
            "Arunachal Pradesh", 
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
            // Union Territories
            "Delhi",
            "Jammu and Kashmir",
            "Ladakh",
            "Chandigarh",
            "Dadra and Nagar Haveli and Daman and Diu",
            "Lakshadweep",
            "Puducherry",
            "Andaman and Nicobar Islands"
        )
        
        // Common districts for major states (fallback data)
        private val COMMON_DISTRICTS = mapOf(
            "Maharashtra" to listOf(
                "Mumbai", "Pune", "Nagpur", "Nashik", "Kolhapur", "Aurangabad", 
                "Solapur", "Amravati", "Thane", "Satara", "Sangli", "Ratnagiri"
            ),
            "Uttar Pradesh" to listOf(
                "Lucknow", "Kanpur", "Agra", "Varanasi", "Prayagraj", "Gorakhpur",
                "Bareilly", "Aligarh", "Meerut", "Ghaziabad", "Noida", "Mathura"
            ),
            "Bihar" to listOf(
                "Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Purnia", "Darbhanga",
                "Araria", "Kishanganj", "Katihar", "Saharsa", "Madhepura", "Supaul"
            ),
            "West Bengal" to listOf(
                "Kolkata", "Howrah", "Hooghly", "Nadia", "Murshidabad", "Malda",
                "Burdwan", "Birbhum", "Bankura", "Purulia", "Jalpaiguri", "Cooch Behar"
            ),
            "Tamil Nadu" to listOf(
                "Chennai", "Coimbatore", "Madurai", "Salem", "Tiruchirappalli", "Vellore",
                "Erode", "Thanjavur", "Tirunelveli", "Kancheepuram", "Cuddalore", "Dindigul"
            ),
            "Karnataka" to listOf(
                "Bangalore", "Mysore", "Mangalore", "Hubli", "Belgaum", "Gulbarga",
                "Bellary", "Bijapur", "Tumkur", "Mandya", "Hassan", "Chitradurga"
            ),
            "Gujarat" to listOf(
                "Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Jamnagar",
                "Anand", "Bharuch", "Kheda", "Mehsana", "Surendranagar", "Gandhinagar"
            ),
            "Rajasthan" to listOf(
                "Jaipur", "Jodhpur", "Udaipur", "Kota", "Bikaner", "Ajmer",
                "Sikar", "Alwar", "Bhilwara", "Pali", "Nagaur", "Jaisalmer"
            ),
            "Madhya Pradesh" to listOf(
                "Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain", "Sagar",
                "Rewa", "Satna", "Chhindwara", "Hoshangabad", "Vidisha", "Dewas"
            ),
            "Andhra Pradesh" to listOf(
                "Visakhapatnam", "Vijayawada", "Guntur", "Nellore", "Kurnool", "Anantapur",
                "Kadapa", "Chittoor", "Prakasam", "Srikakulam", "Vizianagaram", "East Godavari"
            ),
            "Telangana" to listOf(
                "Hyderabad", "Warangal", "Karimnagar", "Nizamabad", "Adilabad", "Khammam",
                "Nalgonda", "Mahbubnagar", "Rangareddy", "Medak", "Nalgonda", "Sangareddy"
            ),
            "Kerala" to listOf(
                "Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kollam", "Alappuzha",
                "Palakkad", "Malappuram", "Kannur", "Kasaragod", "Pathanamthitta", "Idukki"
            ),
            "Punjab" to listOf(
                "Amritsar", "Ludhiana", "Jalandhar", "Patiala", "Bathinda", "Ferozepur",
                "Gurdaspur", "Hoshiarpur", "Kapurthala", "Moga", "Muktsar", "Sangrur"
            ),
            "Haryana" to listOf(
                "Gurgaon", "Faridabad", "Rohtak", "Hisar", "Karnal", "Panipat",
                "Yamunanagar", "Sonipat", "Bhiwani", "Jind", "Sirsa", "Fatehabad"
            ),
            "Delhi" to listOf(
                "New Delhi", "Central Delhi", "North Delhi", "South Delhi", "East Delhi", 
                "West Delhi", "North West Delhi", "South West Delhi", "Shahdara", "Dwarka"
            )
        )
    }
    
    suspend fun getMandiPrices(
        state: String? = null,
        district: String? = null,
        commodity: String? = null,
        market: String? = null,
        limit: Int = 100
    ): Result<List<MandiPrice>> {
        Log.d(TAG, "getMandiPrices: Starting API call with filters - state=$state, district=$district, commodity=$commodity, market=$market")
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "getMandiPrices: Making API call")
                val response = mandiPricesApi.getMandiPrices(limit = limit)
                Log.d(TAG, "getMandiPrices: API call successful - records: ${response.records?.size}")
                
                var prices = response.records?.map { it.toMandiPrice() } ?: emptyList()
                
                // Apply client-side filtering
                prices = prices.filter { price ->
                    (state == null || price.state.equals(state, ignoreCase = true)) &&
                    (district == null || price.district.equals(district, ignoreCase = true)) &&
                    (commodity == null || price.commodityName.equals(commodity, ignoreCase = true)) &&
                    (market == null || price.marketName.equals(market, ignoreCase = true))
                }
                
                Log.d(TAG, "getMandiPrices: Filtered to ${prices.size} records")
                Result.success(prices)
            } catch (e: Exception) {
                Log.e(TAG, "getMandiPrices: API call failed", e)
                Result.failure(e)
            }
        }
    }
    
    suspend fun getAvailableStates(): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mandiPricesApi.getMandiPrices(limit = 1000)
                val apiStates: List<String> = response.records?.mapNotNull { it.state }?.distinct()?.toList() ?: emptyList()
                
                // Merge API states with all Indian states to ensure complete coverage
                val allStates = (ALL_INDIAN_STATES + apiStates).distinct().sorted()
                
                Log.d(TAG, "getAvailableStates: API states: ${apiStates.size}, Total states: ${allStates.size}")
                Result.success(allStates)
            } catch (e: Exception) {
                Log.e(TAG, "getAvailableStates: Failed, using fallback states", e)
                // Fallback to all Indian states if API fails
                Result.success(ALL_INDIAN_STATES.sorted())
            }
        }
    }
    
    suspend fun getAvailableDistricts(state: String? = null): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mandiPricesApi.getMandiPrices(limit = 1000)
                var districts: List<String> = response.records?.mapNotNull { it.district }?.distinct()?.toList() ?: emptyList()
                
                if (state != null) {
                    districts = response.records?.filter { it.state?.equals(state, ignoreCase = true) == true }
                        ?.mapNotNull { it.district }?.distinct()?.toList() ?: emptyList()
                }
                
                // If no districts found from API for the selected state, use fallback districts
                if (state != null && districts.isEmpty()) {
                    val fallbackDistricts = COMMON_DISTRICTS[state] ?: emptyList()
                    Log.d(TAG, "getAvailableDistricts: Using fallback districts for $state: ${fallbackDistricts.size}")
                    districts = fallbackDistricts
                }
                
                Result.success(districts.sorted())
            } catch (e: Exception) {
                Log.e(TAG, "getAvailableDistricts: Failed, using fallback districts", e)
                // Use fallback districts if API fails
                val fallbackDistricts = if (state != null) COMMON_DISTRICTS[state] ?: emptyList() else emptyList()
                Result.success(fallbackDistricts.sorted())
            }
        }
    }
    
    suspend fun getAvailableCommodities(): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mandiPricesApi.getMandiPrices(limit = 1000)
                val commodities: List<String> = response.records?.mapNotNull { it.commodity }?.distinct()?.toList() ?: emptyList()
                Result.success(commodities.sorted())
            } catch (e: Exception) {
                Log.e(TAG, "getAvailableCommodities: Failed", e)
                Result.failure(e)
            }
        }
    }
    
    suspend fun getAvailableMarkets(state: String? = null, district: String? = null): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mandiPricesApi.getMandiPrices(limit = 1000)
                var markets: List<String> = response.records?.mapNotNull { it.market }?.distinct()?.toList() ?: emptyList()
                
                if (state != null || district != null) {
                    markets = response.records?.filter { record ->
                        (state == null || record.state?.equals(state, ignoreCase = true) == true) &&
                        (district == null || record.district?.equals(district, ignoreCase = true) == true)
                    }?.mapNotNull { it.market }?.distinct()?.toList() ?: emptyList()
                }
                
                Result.success(markets.sorted())
            } catch (e: Exception) {
                Log.e(TAG, "getAvailableMarkets: Failed", e)
                Result.failure(e)
            }
        }
    }
} 