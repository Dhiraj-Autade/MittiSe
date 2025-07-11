package com.example.mittise.di

import android.util.Log
import com.example.mittise.data.api.MarketplaceApi
import com.example.mittise.data.api.WeatherApi
import com.example.mittise.data.repository.WeatherRepository
import com.example.mittise.data.repository.MarketplaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        Log.d(TAG, "provideRetrofit: Creating default retrofit")
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // This will be replaced with actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(): Retrofit {
        Log.d(TAG, "provideWeatherRetrofit: Creating weather retrofit")
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMarketplaceApi(): MarketplaceApi {
        Log.d(TAG, "provideMarketplaceApi: Creating fake marketplace API")
        return com.example.mittise.data.api.FakeMarketplaceApi()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weather") retrofit: Retrofit): WeatherApi {
        Log.d(TAG, "provideWeatherApi: Creating weather API")
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApi: WeatherApi): WeatherRepository {
        Log.d(TAG, "provideWeatherRepository: Creating weather repository")
        return WeatherRepository(weatherApi)
    }

    @Provides
    @Singleton
    fun provideMarketplaceRepository(marketplaceApi: MarketplaceApi): MarketplaceRepository {
        Log.d(TAG, "provideMarketplaceRepository: Creating marketplace repository")
        return MarketplaceRepository(marketplaceApi)
    }
} 