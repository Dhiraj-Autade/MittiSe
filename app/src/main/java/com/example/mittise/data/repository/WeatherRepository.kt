package com.example.mittise.data.repository

import android.util.Log
import com.example.mittise.data.api.WeatherApi
import com.example.mittise.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    companion object {
        private const val TAG = "WeatherRepository"
    }
    
    private val apiKey = "d4b866ba4927c319160f2311f33a974f"
    
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherResponse> {
        Log.d(TAG, "getCurrentWeather: Starting API call for lat=$lat, lon=$lon")
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "getCurrentWeather: Making API call")
                val response = weatherApi.getCurrentWeather(lat, lon, apiKey)
                Log.d(TAG, "getCurrentWeather: API call successful - ${response.name}")
                Result.success(response)
            } catch (e: Exception) {
                Log.e(TAG, "getCurrentWeather: API call failed", e)
                Result.failure(e)
            }
        }
    }
    
    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherResponse> {
        Log.d(TAG, "getWeatherForecast: Starting API call for lat=$lat, lon=$lon")
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "getWeatherForecast: Making API call")
                val response = weatherApi.getWeatherForecast(lat, lon, apiKey)
                Log.d(TAG, "getWeatherForecast: API call successful - forecast items: ${response.forecastList?.size}")
                Result.success(response)
            } catch (e: Exception) {
                Log.e(TAG, "getWeatherForecast: API call failed", e)
                Result.failure(e)
            }
        }
    }
} 