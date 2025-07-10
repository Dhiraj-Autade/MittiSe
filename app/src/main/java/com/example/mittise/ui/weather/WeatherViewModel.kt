package com.example.mittise.ui.weather

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.WeatherResponse
import com.example.mittise.data.model.WeatherAnalysis
import com.example.mittise.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    application: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "WeatherViewModel"
    }

    var weatherData by mutableStateOf<WeatherResponse?>(null)
        private set
    
    var forecastData by mutableStateOf<WeatherResponse?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var error by mutableStateOf<String?>(null)
        private set
    
    var location by mutableStateOf<Location?>(null)
        private set
    
    var hasLocationPermission by mutableStateOf(false)
        private set
    
    var isLocationServiceEnabled by mutableStateOf(true)
        private set

    var weatherAnalysis by mutableStateOf<WeatherAnalysis?>(null)
        private set

    fun checkLocationPermission(context: Context) {
        Log.d(TAG, "checkLocationPermission: Starting permission check")
        
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        Log.d(TAG, "checkLocationPermission: FINE_LOCATION = $fineLocation, COARSE_LOCATION = $coarseLocation")
        
        hasLocationPermission = fineLocation || coarseLocation
        
        Log.d(TAG, "checkLocationPermission: hasLocationPermission = $hasLocationPermission")
        
        // If we have permission, try to get location immediately
        if (hasLocationPermission) {
            Log.d(TAG, "checkLocationPermission: Permission granted, getting location")
            getCurrentLocation(context)
        } else {
            Log.d(TAG, "checkLocationPermission: Permission not granted, need to request")
        }
    }

    fun checkLocationServicesEnabled(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        isLocationServiceEnabled = isGpsEnabled || isNetworkEnabled
    }

    fun getCurrentLocation(context: Context) {
        Log.d(TAG, "getCurrentLocation: Starting location request")
        
        if (!hasLocationPermission) {
            Log.w(TAG, "getCurrentLocation: Location permission not granted")
            error = "Location permission not granted"
            return
        }
        checkLocationServicesEnabled(context)
        if (!isLocationServiceEnabled) {
            Log.w(TAG, "getCurrentLocation: Location services are OFF")
            error = "Location services are OFF"
            isLoading = false
            return
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "getCurrentLocation: Setting loading state")
                isLoading = true
                error = null
                
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                Log.d(TAG, "getCurrentLocation: LocationManager obtained")
                
                // Check if location services are enabled
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                
                Log.d(TAG, "getCurrentLocation: GPS enabled = $isGpsEnabled, Network enabled = $isNetworkEnabled")
                
                // Try to get last known location first
                var currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                Log.d(TAG, "getCurrentLocation: GPS location = ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                
                if (currentLocation == null) {
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    Log.d(TAG, "getCurrentLocation: Network location = ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                }
                
                if (currentLocation == null) {
                    // Try passive provider as last resort
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                    Log.d(TAG, "getCurrentLocation: Passive location = ${currentLocation?.latitude}, ${currentLocation?.longitude}")
                }
                
                if (currentLocation != null) {
                    Log.d(TAG, "getCurrentLocation: Location obtained successfully: ${currentLocation.latitude}, ${currentLocation.longitude}")
                    location = currentLocation
                    fetchWeatherData(currentLocation.latitude, currentLocation.longitude)
                    fetchForecastData(currentLocation.latitude, currentLocation.longitude)
                } else {
                    Log.w(TAG, "getCurrentLocation: Unable to get current location")
                    error = "Unable to get current location. Please ensure location services are enabled."
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "getCurrentLocation: Error getting location", e)
                error = "Error getting location: ${e.message}"
                isLoading = false
            }
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        Log.d(TAG, "fetchWeatherData: Fetching weather for lat=$lat, lon=$lon")
        viewModelScope.launch {
            try {
                val result = weatherRepository.getCurrentWeather(lat, lon)
                result.fold(
                    onSuccess = { response ->
                        Log.d(TAG, "fetchWeatherData: Success - ${response.name}, temp=${response.main?.temp}")
                        weatherData = response
                        if (forecastData != null) {
                            isLoading = false
                            analyzeWeatherConditions()
                        }
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "fetchWeatherData: Error fetching weather", exception)
                        error = "Error fetching weather: ${exception.message}"
                        if (forecastData != null) {
                            isLoading = false
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "fetchWeatherData: Exception fetching weather", e)
                error = "Error fetching weather: ${e.message}"
                if (forecastData != null) {
                    isLoading = false
                }
            }
        }
    }

    private fun fetchForecastData(lat: Double, lon: Double) {
        Log.d(TAG, "fetchForecastData: Fetching forecast for lat=$lat, lon=$lon")
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeatherForecast(lat, lon)
                result.fold(
                    onSuccess = { response ->
                        Log.d(TAG, "fetchForecastData: Success - forecast items: ${response.forecastList?.size}")
                        forecastData = response
                        if (weatherData != null) {
                            isLoading = false
                            analyzeWeatherConditions()
                        }
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "fetchForecastData: Error fetching forecast", exception)
                        error = "Error fetching forecast: ${exception.message}"
                        if (weatherData != null) {
                            isLoading = false
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "fetchForecastData: Exception fetching forecast", e)
                error = "Error fetching forecast: ${e.message}"
                if (weatherData != null) {
                    isLoading = false
                }
            }
        }
    }

    fun refreshWeather() {
        Log.d(TAG, "refreshWeather: Refreshing weather data")
        location?.let { loc ->
            isLoading = true
            fetchWeatherData(loc.latitude, loc.longitude)
            fetchForecastData(loc.latitude, loc.longitude)
        } ?: run {
            Log.w(TAG, "refreshWeather: No location available")
        }
    }

    fun analyzeWeatherConditions() {
        Log.d(TAG, "analyzeWeatherConditions: Starting weather analysis")
        
        if (weatherData == null && forecastData == null) {
            Log.w(TAG, "analyzeWeatherConditions: No weather data available for analysis")
            return
        }
        
        viewModelScope.launch {
            try {
                val analysis = performWeatherAnalysis()
                weatherAnalysis = analysis
                Log.d(TAG, "analyzeWeatherConditions: Analysis completed - ${analysis.condition}")
            } catch (e: Exception) {
                Log.e(TAG, "analyzeWeatherConditions: Error analyzing weather", e)
            }
        }
    }
    
    private fun performWeatherAnalysis(): WeatherAnalysis {
        val currentWeather = weatherData?.weather?.firstOrNull()
        val currentTemp = weatherData?.main?.temp ?: 0.0
        val currentHumidity = weatherData?.main?.humidity ?: 0
        val currentWindSpeed = weatherData?.wind?.speed ?: 0.0
        val currentPressure = weatherData?.main?.pressure ?: 0
        
        // Analyze forecast for rain probability
        val forecastItems = forecastData?.forecastList ?: emptyList()
        val rainProbability = forecastItems.take(8).map { it.pop }.average() // Next 24 hours
        val hasHeavyRain = forecastItems.any { it.pop > 0.7 }
        val hasModerateRain = forecastItems.any { it.pop in 0.3..0.7 }
        
        return when {
            // Heavy Rain Analysis
            hasHeavyRain || (currentWeather?.main?.contains("Rain", ignoreCase = true) == true && rainProbability > 0.6) -> {
                WeatherAnalysis(
                    condition = "Heavy Rainfall Expected",
                    severity = "High",
                    farmingAdvice = listOf(
                        "Avoid field operations during heavy rain",
                        "Ensure proper drainage in fields",
                        "Protect harvested crops from moisture",
                        "Postpone pesticide applications"
                    ),
                    cropRecommendations = listOf(
                        "Rice and paddy crops will benefit",
                        "Avoid sowing drought-resistant crops",
                        "Consider waterlogging-resistant varieties"
                    ),
                    precautions = listOf(
                        "Check field drainage systems",
                        "Secure farm equipment and tools",
                        "Monitor for waterlogging",
                        "Prepare for delayed harvesting"
                    ),
                    optimalActivities = listOf(
                        "Indoor seed preparation",
                        "Equipment maintenance",
                        "Market research and planning",
                        "Crop storage preparation"
                    )
                )
            }
            
            // Moderate Rain Analysis
            hasModerateRain || (currentWeather?.main?.contains("Rain", ignoreCase = true) == true && rainProbability > 0.3) -> {
                WeatherAnalysis(
                    condition = "Moderate Rainfall",
                    severity = "Medium",
                    farmingAdvice = listOf(
                        "Light rain is beneficial for most crops",
                        "Good time for sowing rain-fed crops",
                        "Monitor soil moisture levels",
                        "Avoid heavy machinery operations"
                    ),
                    cropRecommendations = listOf(
                        "Ideal for sowing rice, maize, and pulses",
                        "Good for transplanting seedlings",
                        "Consider early-season crops"
                    ),
                    precautions = listOf(
                        "Avoid over-irrigation",
                        "Monitor for fungal diseases",
                        "Check field accessibility"
                    ),
                    optimalActivities = listOf(
                        "Sowing operations",
                        "Transplanting",
                        "Light field preparation",
                        "Organic manure application"
                    )
                )
            }
            
            // Clear/Sunny Weather Analysis
            currentWeather?.main?.contains("Clear", ignoreCase = true) == true || 
            (currentTemp > 25 && currentHumidity < 60) -> {
                WeatherAnalysis(
                    condition = "Clear and Sunny Weather",
                    severity = "Low",
                    farmingAdvice = listOf(
                        "Excellent conditions for field operations",
                        "Good for harvesting and drying",
                        "Monitor irrigation needs",
                        "Ideal for pest control applications"
                    ),
                    cropRecommendations = listOf(
                        "Good for wheat, barley, and oilseeds",
                        "Ideal for harvesting operations",
                        "Consider drought-resistant varieties"
                    ),
                    precautions = listOf(
                        "Ensure adequate irrigation",
                        "Protect crops from heat stress",
                        "Monitor soil moisture",
                        "Avoid midday operations in extreme heat"
                    ),
                    optimalActivities = listOf(
                        "Harvesting operations",
                        "Field preparation",
                        "Pest control applications",
                        "Crop drying and storage"
                    )
                )
            }
            
            // Cloudy Weather Analysis
            currentWeather?.main?.contains("Cloud", ignoreCase = true) == true || 
            (currentTemp in 15.0..25.0 && currentHumidity > 70) -> {
                WeatherAnalysis(
                    condition = "Cloudy Weather",
                    severity = "Low",
                    farmingAdvice = listOf(
                        "Moderate conditions for farming activities",
                        "Good for transplanting and sowing",
                        "Monitor for fungal diseases",
                        "Ideal for organic farming activities"
                    ),
                    cropRecommendations = listOf(
                        "Good for vegetables and leafy crops",
                        "Ideal for transplanting seedlings",
                        "Consider shade-loving crops"
                    ),
                    precautions = listOf(
                        "Monitor humidity levels",
                        "Check for fungal infections",
                        "Ensure proper ventilation in greenhouses"
                    ),
                    optimalActivities = listOf(
                        "Transplanting operations",
                        "Vegetable cultivation",
                        "Organic farming activities",
                        "Greenhouse management"
                    )
                )
            }
            
            // Windy Weather Analysis
            currentWindSpeed > 15.0 -> {
                WeatherAnalysis(
                    condition = "Windy Conditions",
                    severity = "Medium",
                    farmingAdvice = listOf(
                        "Avoid spraying operations",
                        "Secure loose materials and equipment",
                        "Monitor for crop lodging",
                        "Postpone aerial applications"
                    ),
                    cropRecommendations = listOf(
                        "Avoid tall crops prone to lodging",
                        "Consider wind-resistant varieties",
                        "Good for wind-pollinated crops"
                    ),
                    precautions = listOf(
                        "Secure farm structures",
                        "Avoid open field operations",
                        "Monitor crop stability",
                        "Check irrigation systems"
                    ),
                    optimalActivities = listOf(
                        "Equipment maintenance",
                        "Indoor activities",
                        "Planning and documentation",
                        "Market research"
                    )
                )
            }
            
            // Default Analysis
            else -> {
                WeatherAnalysis(
                    condition = "Normal Weather Conditions",
                    severity = "Low",
                    farmingAdvice = listOf(
                        "Continue with regular farming activities",
                        "Monitor weather forecasts regularly",
                        "Maintain routine crop care",
                        "Plan for seasonal activities"
                    ),
                    cropRecommendations = listOf(
                        "Follow seasonal crop calendar",
                        "Consider local climate patterns",
                        "Choose crops based on soil type"
                    ),
                    precautions = listOf(
                        "Regular monitoring of crops",
                        "Maintain farm records",
                        "Stay updated with weather forecasts"
                    ),
                    optimalActivities = listOf(
                        "Regular farming operations",
                        "Crop monitoring and care",
                        "Soil testing and preparation",
                        "Farm planning and management"
                    )
                )
            }
        }
    }
} 