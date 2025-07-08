package com.example.mittise

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mittise.navigation.MittiSeNavigation
import com.example.mittise.ui.theme.MittiSeTheme
import com.example.mittise.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply saved language before setting content
        LocaleHelper.onAttach(this)
        
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setContent {
            MittiSeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MittiSeApp()
                    }
                }
            }
        }
    }
    
    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
    
    override fun onResume() {
        super.onResume()
        // Ensure locale is applied when resuming
        LocaleHelper.onAttach(this)
    }
    

}

@Composable
fun MittiSeApp() {
    val navController = rememberNavController()
    
    // For now, we'll keep the existing navigation structure
    // This will be gradually replaced as we migrate screens
    MittiSeNavigation(navController = navController)
} 