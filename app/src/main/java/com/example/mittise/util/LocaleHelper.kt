package com.example.mittise.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleHelper {
    
    private const val SELECTED_LANGUAGE = "language"
    
    fun onAttach(context: Context): Context {
        val language = getPersistedLanguage(context, Locale.getDefault().language)
        return setLocale(context, language)
    }
    
    fun onAttach(context: Context, defaultLanguage: String): Context {
        val language = getPersistedLanguage(context, defaultLanguage)
        return setLocale(context, language)
    }
    
    fun getLanguage(context: Context): String? {
        return getPersistedLanguage(context, Locale.getDefault().language)
    }
    
    fun setLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }
    
    fun forceUpdateLocale(context: Context): Context {
        val language = getPersistedLanguage(context, Locale.getDefault().language)
        return setLocale(context, language)
    }
    
    private fun getPersistedLanguage(context: Context, defaultLanguage: String): String {
        val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
    }
    
    private fun persistLanguage(context: Context, language: String) {
        val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply()
    }
    
    @Suppress("DEPRECATION")
    private fun updateResources(context: Context, locale: Locale): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        return context.createConfigurationContext(configuration)
    }
    
    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val resources: Resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        
        return context
    }
    
    fun getSupportedLanguages(): Map<String, String> {
        return mapOf(
            "en" to "English",
            "hi" to "हिंदी",
            "mr" to "मराठी",
            "ta" to "தமிழ்",
            "te" to "తెలుగు",
            "ml" to "മലയാളം"
        )
    }
    
    fun getLanguageDisplayName(languageCode: String): String {
        return getSupportedLanguages()[languageCode] ?: languageCode
    }
    
    fun isLanguageSupported(languageCode: String): Boolean {
        return getSupportedLanguages().containsKey(languageCode)
    }
} 