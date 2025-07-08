package com.example.mittise.ui.language

import android.app.Application
import android.content.Intent
import android.os.Process
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.MainActivityCompose
import com.example.mittise.util.LocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _currentLanguage = MutableStateFlow(LocaleHelper.getLanguage(application) ?: "en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    
    private val _supportedLanguages = MutableStateFlow(LocaleHelper.getSupportedLanguages())
    val supportedLanguages: StateFlow<Map<String, String>> = _supportedLanguages.asStateFlow()
    
    fun setLanguage(languageCode: String) {
        if (LocaleHelper.isLanguageSupported(languageCode)) {
            viewModelScope.launch {
                val context = getApplication<Application>()
                
                // Save the language preference
                LocaleHelper.setLocale(context, languageCode)
                _currentLanguage.value = languageCode
                
                // Small delay to ensure the preference is saved
                delay(100)
                
                // Force complete app restart
                restartApplication()
            }
        }
    }
    
    private fun restartApplication() {
        val context = getApplication<Application>()
        
        // Create intent to restart the app
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        
        // Kill the current process and restart
        context.startActivity(intent)
        Process.killProcess(Process.myPid())
        System.exit(0)
    }
    
    fun getLanguageDisplayName(languageCode: String): String {
        return LocaleHelper.getLanguageDisplayName(languageCode)
    }
    
    fun isCurrentLanguage(languageCode: String): Boolean {
        return _currentLanguage.value == languageCode
    }
} 