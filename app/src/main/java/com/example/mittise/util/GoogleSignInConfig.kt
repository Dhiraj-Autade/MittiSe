package com.example.mittise.util

object GoogleSignInConfig {
    // INSTRUCTIONS TO GET WEB CLIENT ID:
    // 
    // Method 1: Add Web App to Firebase
    // 1. Go to Firebase Console → Project Settings → General
    // 2. Click "Add app" (</> icon for Web)
    // 3. Register the web app
    // 4. Go back to Project Settings → Your apps → Web app
    // 5. Copy the "Web client ID"
    //
    // Method 2: Extract from google-services.json
    // 1. Open your google-services.json file
    // 2. Look for "oauth_client" array
    // 3. Find entry with "client_type": 3 (web client)
    // 4. Copy the "client_id" value
    //
    // Method 3: Google Cloud Console
    // 1. Go to console.cloud.google.com
    // 2. Select your Firebase project
    // 3. Go to APIs & Services → Credentials
    // 4. Find OAuth 2.0 Client ID with type "Web application"
    
    const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID_HERE"
    
    // Set this to false when you have the proper Web Client ID
    const val USE_FALLBACK = true
    
    // You can also add other configuration constants here
    const val REQUEST_CODE_SIGN_IN = 9001
} 