package com.example.mittise

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.mittise.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MittiSeApplication : Application() {
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
    
    override fun onCreate() {
        super.onCreate()
        // Apply saved language at application startup
        LocaleHelper.onAttach(this)
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes (like language changes)
        LocaleHelper.onAttach(this)
    }
    
    override fun getResources(): android.content.res.Resources {
        val locale = Locale(LocaleHelper.getLanguage(this) ?: "en")
        Locale.setDefault(locale)
        
        val config = Configuration(super.getResources().configuration)
        config.setLocale(locale)
        
        return createConfigurationContext(config).resources
    }
    
    override fun getBaseContext(): Context {
        return LocaleHelper.onAttach(super.getBaseContext())
    }
} 