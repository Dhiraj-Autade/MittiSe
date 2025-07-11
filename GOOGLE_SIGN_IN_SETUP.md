# Google Sign-In Setup Guide

This guide will help you set up Google Sign-In for your MittiSe Android app.

## Prerequisites

1. A Google Cloud Console project
2. Firebase project configured for your app
3. Google Services JSON file (`google-services.json`) in your app directory

## Setup Steps

### 1. Enable Google Sign-In in Firebase Console

1. Go to your [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Go to **Authentication** > **Sign-in method**
4. Enable **Google** as a sign-in provider
5. Add your support email

### 2. Get Your Web Client ID

1. In Firebase Console, go to **Project Settings** > **General**
2. Scroll down to **Your apps** section
3. Find your Android app and click on it
4. In the **General** tab, you'll see your **Web client ID**
5. Copy this Web client ID

### 3. Update Your App Configuration

1. Open `app/src/main/java/com/example/mittise/util/GoogleSignInConfig.kt`
2. Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual Web client ID from step 2:

```kotlin
const val WEB_CLIENT_ID = "123456789-abcdefghijklmnop.apps.googleusercontent.com"
```

### 4. Add SHA-1 Fingerprint (if not already added)

1. Get your app's SHA-1 fingerprint:
   ```bash
   # For debug builds
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   
   # For release builds (replace with your keystore path)
   keytool -list -v -keystore your-keystore.jks -alias your-alias
   ```

2. Add the SHA-1 fingerprint to your Firebase project:
   - Go to Firebase Console > Project Settings > General
   - Scroll to **Your apps** section
   - Click on your Android app
   - Click **Add fingerprint**
   - Paste your SHA-1 fingerprint

### 5. Test the Integration

1. Build and run your app
2. Go to the Login or Sign Up screen
3. Tap the "Sign in with Google" or "Sign up with Google" button
4. You should see the Google Sign-In dialog
5. After successful sign-in, you should be redirected to the main app

## Troubleshooting

### Common Issues

1. **"Google Sign-In failed" error**
   - Check that your Web client ID is correct
   - Verify SHA-1 fingerprint is added to Firebase
   - Ensure Google Sign-In is enabled in Firebase Console

2. **"Network error"**
   - Check your internet connection
   - Verify Firebase project is properly configured

3. **"Invalid credential" error**
   - Make sure you're using the correct Web client ID
   - Check that the Google Sign-In provider is enabled in Firebase

### Debug Tips

1. Check the Android logs for detailed error messages
2. Verify your `google-services.json` file is up to date
3. Make sure you're testing on a device/emulator with Google Play Services

## Security Notes

- Never commit your actual Web client ID to version control
- Consider using BuildConfig or environment variables for production
- Regularly rotate your OAuth 2.0 client secrets

## Additional Resources

- [Firebase Authentication Documentation](https://firebase.google.com/docs/auth)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android)
- [Firebase Console](https://console.firebase.google.com/) 