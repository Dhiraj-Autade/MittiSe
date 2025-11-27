# MittiSe - Smart Farming Assistant

<div align="center">

![MittiSe Logo](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

**Empowering Indian Farmers with Technology**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com/)

</div>

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Modules](#modules)
- [APIs Used](#apis-used)
- [Database](#database)
- [Setup Instructions](#setup-instructions)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Permissions](#permissions)
- [Language Support](#language-support)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

---

## 🌾 Overview

**MittiSe** is a comprehensive Android application designed to assist Indian farmers with modern technology solutions. The app provides real-time weather updates, market prices (APMC/Mandi rates), AI-powered agricultural advice, a marketplace for buying/selling products, social networking features, and much more.

Built with **Jetpack Compose** and **Kotlin**, MittiSe follows modern Android development practices including **MVVM architecture**, **Dependency Injection with Hilt**, and **Firebase integration**.

---

## ✨ Features

### 🔐 Authentication
- Email/Password authentication
- Google Sign-In integration
- Firebase Authentication backend
- Profile management with Firestore

### 📊 Dashboard
- Quick access to all features
- Live news updates (agriculture-focused)
- Weather widget
- Quick actions for common tasks

### 🌤️ Weather Forecasting
- Real-time weather data
- Location-based weather updates
- 5-day weather forecast
- Agricultural weather insights
- Temperature, humidity, wind speed
- Weather condition analysis for farming

### 🤖 AI Chatbot (Gemini Integration)
- Google Gemini AI-powered farming assistant
- Voice input support (Speech-to-Text)
- Context-aware responses for Indian farming
- Farming tips, pest control advice
- Crop recommendations
- Market insights

### 🏪 Marketplace
- Buy and sell agricultural products
- Product categories: Seeds, Equipment, Pesticides, Fertilizers, Grains
- Image upload for product listings
- Firebase Firestore for product storage
- Category filtering
- User product management

### 📈 APMC/Mandi Prices
- market prices from Government of India API
- State and district filtering
- Commodity-specific price search
- Historical price trends
- Arrival quantity information

### 📰 News Feed
- agricultural news updates
- India-specific agricultural content

### 📅 Crop Calendar
- Seasonal crop planning
- Planting and harvesting schedules
- Activity tracking

---

## 🛠️ Tech Stack

### **Frontend**
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - UI components and theming
- **Compose Navigation** - Navigation component
- **Lottie Animations** - Engaging UI animations
- **Coil** - Image loading for Compose

### **Backend & Cloud**
- **Firebase Authentication** - User authentication
- **Firebase Firestore** - NoSQL cloud database
- **Firebase Storage** - File and image storage
- **Google Gemini AI** - Chatbot intelligence

### **Architecture & Patterns**
- **MVVM (Model-View-ViewModel)** - Architecture pattern
- **Repository Pattern** - Data layer abstraction
- **Dependency Injection** - Hilt/Dagger
- **Kotlin Coroutines** - Asynchronous programming
- **StateFlow** - Reactive state management

### **Networking**
- **Retrofit** - REST API client
- **OkHttp** - HTTP client
- **Gson** - JSON serialization

### **Local Storage**
- **Room Database** - SQLite abstraction
- **SharedPreferences** - Key-value storage
- **DataStore** - Modern data storage solution

### **Testing**
- **JUnit** - Unit testing
- **Espresso** - UI testing
- **AndroidX Test** - Testing framework

---

## 🏗️ Architecture

MittiSe follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:

```
┌─────────────────────────────────────────────┐
│              UI Layer (Compose)              │
│  ┌───────────┐  ┌───────────┐  ┌──────────┐│
│  │  Screens  │  │ Components│  │  Theme   ││
│  └─────┬─────┘  └─────┬─────┘  └────┬─────┘│
└────────┼──────────────┼─────────────┼──────┘
         │              │             │
┌────────▼──────────────▼─────────────▼──────┐
│           ViewModel Layer (StateFlow)       │
│  ┌──────────────┐  ┌───────────────────┐   │
│  │ AuthViewModel│  │ WeatherViewModel  │   │
│  │ProfileViewModel│ │ ChatbotViewModel │   │
│  │ ApmcViewModel│  │MarketplaceViewModel│  │
│  └──────┬───────┘  └─────────┬─────────┘   │
└─────────┼────────────────────┼─────────────┘
          │                    │
┌─────────▼────────────────────▼─────────────┐
│            Repository Layer                 │
│  ┌──────────────┐  ┌──────────────────┐    │
│  │AuthRepository│  │WeatherRepository │    │
│  │MandiPricesRepo│ │MarketplaceRepo   │    │
│  └──────┬───────┘  └─────────┬────────┘    │
└─────────┼────────────────────┼─────────────┘
          │                    │
┌─────────▼────────────────────▼─────────────┐
│              Data Sources                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │ Firebase │  │  APIs    │  │Room DB   │ │
│  │(Auth/    │  │(Weather, │  │(Local    │ │
│  │Firestore)│  │News,     │  │Cache)    │ │
│  │          │  │Mandi)    │  │          │ │
│  └──────────┘  └──────────┘  └──────────┘ │
└─────────────────────────────────────────────┘
```

### Key Principles:
- **Separation of Concerns**: Each layer has a specific responsibility
- **Dependency Injection**: Hilt manages dependencies
- **Reactive Programming**: StateFlow for UI state management
- **Single Source of Truth**: Repository pattern for data
- **Unidirectional Data Flow**: Data flows from repository → ViewModel → UI

---

## 📦 Modules

### **1. Authentication Module**
- **Package**: `com.example.mittise.ui.screens` & `com.example.mittise.ui.viewmodels`
- **Components**:
  - `LoginScreen` - User login interface
  - `SignUpScreen` - New user registration
  - `AuthViewModel` - Authentication state management
  - `AuthRepository` - Firebase Auth integration
  - `GoogleSignInHelper` - Google Sign-In implementation

### **2. Dashboard Module**
- **Package**: `com.example.mittise.ui.screens`
- **Components**:
  - `DashboardScreen` - Main landing page
  - Quick access cards
  - News feed integration
  - Weather widget
  - Category navigation

### **3. Weather Module**
- **Package**: `com.example.mittise.ui.weather`
- **Components**:
  - `WeatherViewModel` - Weather data management
  - `WeatherRepository` - OpenWeatherMap API integration
  - Weather forecast display
  - Location-based services

### **4. Chatbot Module**
- **Package**: `com.example.mittise.ui.viewmodels` & `com.example.mittise.data.api`
- **Components**:
  - `ChatbotViewModel` - Chat state management
  - `GeminiService` - Google Gemini AI integration
  - Speech-to-text support
  - Farming-focused AI assistant

### **5. Marketplace Module**
- **Package**: `com.example.mittise.ui.marketplace`
- **Components**:
  - `MarketplaceViewModel` - Product management
  - `MarketplaceScreen` - Product listing UI
  - `FarmerProductRegistration` - Add/Edit products
  - Firestore integration for products
  - Image upload with Firebase Storage

### **6. APMC/Mandi Prices Module**
- **Package**: `com.example.mittise.ui.apmc`
- **Components**:
  - `ApmcViewModel` - Mandi prices management
  - `MandiPricesRepository` - Gov API integration
  - `MandiPriceCard` - Price display component
  - State/District/Commodity filtering

### **7. Profile Module**
- **Package**: `com.example.mittise.ui.viewmodels` & `com.example.mittise.ui.screens`
- **Components**:
  - `ProfileViewModel` - User profile management
  - `ProfileScreen` - Profile display
  - `EditProfileScreen` - Profile editing
  - Firestore user data

### **8. News Module**
- **Package**: `com.example.mittise.ui.viewmodels` & `com.example.mittise.data`
- **Components**:
  - `NewsViewModel` - News management
  - `NewsApiService` - News API integration
  - Live agriculture news

### **9. Language Module**
- **Package**: `com.example.mittise.ui.language`
- **Components**:
  - `LanguageViewModel` - Language management
  - `LocaleHelper` - Localization utilities
  - Multi-language support

### **10. Navigation Module**
- **Package**: `com.example.mittise.navigation`
- **Components**:
  - `Screen` - Route definitions
  - `MittiSeNavigation` - Navigation graph
  - Drawer navigation
  - Bottom navigation

---

## 🌐 APIs Used

### **1. OpenWeatherMap API**
- **Purpose**: Real-time weather data
- **Endpoint**: `https://api.openweathermap.org/data/2.5/`
- **Features**: Current weather, 5-day forecast
- **API Key**: Integrated in `WeatherRepository`
- **Documentation**: [OpenWeatherMap Docs](https://openweathermap.org/api)

### **2. Government of India - Mandi Prices API**
- **Purpose**: Live agricultural market prices
- **Endpoint**: `https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070`
- **Features**: Commodity prices, state/district filtering
- **API Key**: Integrated in `MandiPricesRepository`
- **Data**: Min/Max/Modal prices, arrival quantities
- **Documentation**: [data.gov.in](https://data.gov.in/)

### **3. News API**
- **Purpose**: Agricultural news updates
- **Endpoint**: `https://newsapi.org/v2/top-headlines`
- **Configuration**: 
  - Country: India (`in`)
  - Category: General (filtered for agriculture)
  - API Key: `8e3a4551e04f4ff08c9dd0712ca6ff08`
- **Documentation**: [News API Docs](https://newsapi.org/docs)

### **4. Google Gemini AI API**
- **Purpose**: AI-powered chatbot
- **Model**: `gemini-pro`
- **Features**: 
  - Farming advice
  - Pest control guidance
  - Crop recommendations
  - Context-aware responses
- **API Key**: Integrated in `GeminiService`
- **Documentation**: [Google AI Docs](https://ai.google.dev/docs)


### **6. Firebase Services**
- **Authentication**: User management
- **Firestore**: NoSQL database
- **Storage**: Image/file storage
- **Configuration**: `google-services.json`

---

## 💾 Database

### **Firebase Firestore (Cloud Database)**

#### Collections Structure:

```
firestore/
├── users/
│   ├── {userId}/
│   │   ├── firstName: String
│   │   ├── lastName: String
│   │   ├── email: String
│   │   ├── phoneNumber: String?
│   │   ├── bio: String?
│   │   ├── farmSize: String?
│   │   ├── cropsGrown: String?
│   │   ├── yearsExperience: String?
│   │   ├── profileImageUrl: String?
│   │   ├── createdAt: Timestamp
│   │   ├── signInMethod: String
│   │   └── marketplace_products/
│   │       └── {productId}/
│   │           ├── productName: String
│   │           ├── productType: String
│   │           ├── quantity: Double
│   │           ├── unit: String
│   │           ├── expectedPrice: Double
│   │           ├── priceUnit: String
│   │           ├── location: String
│   │           ├── farmerName: String
│   │           ├── farmerContact: String
│   │           ├── description: String
│   │           ├── imageUrl: String?
│   │           ├── isAvailable: Boolean
│   │           ├── createdAt: Timestamp
│   │           └── userId: String
```

### **Room Database (Local Cache)**

#### Tables:

```sql
-- Mandi Prices Cache
CREATE TABLE mandi_prices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    commodityName TEXT NOT NULL,
    commodityVariety TEXT,
    marketName TEXT,
    marketCode TEXT,
    district TEXT,
    state TEXT,
    minPrice REAL,
    maxPrice REAL,
    modalPrice REAL,
    priceUnit TEXT,
    date TEXT,
    arrivalQuantity REAL,
    arrivalUnit TEXT,
    source TEXT,
    timestamp INTEGER
);
```

#### Entities:
- `MandiPriceEntity` - Caches mandi prices locally
- `MandiPriceDao` - Data Access Object for queries

### **SharedPreferences**
- Language preferences
- User settings
- App configuration

---

## 🚀 Setup Instructions

### Prerequisites
- Android Studio (Latest version recommended)
- JDK 8 or higher
- Android SDK (API 24+)
- Firebase Account
- API Keys (Weather, News, Gemini, Mandi)

### Step 1: Clone Repository
```bash
git clone https://github.com/Dhiraj-Autade/MittiSe.git
cd MittiSe
```

### Step 2: Configure Firebase
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app to your project
3. Download `google-services.json`
4. Place it in `app/` directory
5. Enable **Authentication** (Email/Password, Google)
6. Enable **Firestore Database**
7. Enable **Firebase Storage**

### Step 3: Configure Google Sign-In
1. Go to Firebase Console → Authentication → Sign-in method
2. Enable Google Sign-In
3. Get your **Web Client ID**
4. Update `GoogleSignInConfig.kt`:
```kotlin
const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID_HERE"
```
5. Add SHA-1 fingerprint:
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Step 4: Configure API Keys

#### OpenWeatherMap
1. Sign up at [OpenWeatherMap](https://openweathermap.org/api)
2. Get your API key
3. Update `WeatherRepository.kt`:
```kotlin
private val apiKey = "YOUR_OPENWEATHER_API_KEY"
```

#### News API
1. Sign up at [NewsAPI](https://newsapi.org/)
2. Get your API key
3. Update `Constants.kt`:
```kotlin
const val NEWS_API_KEY = "YOUR_NEWS_API_KEY"
```

#### Google Gemini AI
1. Get API key from [Google AI Studio](https://ai.google.dev/)
2. Update `ChatbotViewModel.kt`:
```kotlin
private val aiService: AIService = GeminiService("YOUR_GEMINI_API_KEY")
```

#### Mandi Prices API
- Already configured with Government API key
- No additional setup needed

### Step 5: Build & Run
1. Open project in Android Studio
2. Sync Gradle files
3. Connect Android device or start emulator
4. Click **Run** (Shift + F10)

### Step 6: Language Setup
The app supports multiple languages:
- English (en)
- Hindi (hi)
- Marathi (mr)
- Tamil (ta)
- Telugu (te)
- Malayalam (ml)

Resources are in `app/src/main/res/values-{language}/`

---

## 📁 Project Structure

```
MittiSe/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mittise/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/           # API interfaces
│   │   │   │   │   │   ├── AIService.kt
│   │   │   │   │   │   ├── GeminiService.kt
│   │   │   │   │   │   ├── MandiPricesApi.kt
│   │   │   │   │   │   ├── MarketplaceApi.kt
│   │   │   │   │   │   └── WeatherApi.kt
│   │   │   │   │   ├── local/         # Room database
│   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   ├── MandiPriceDao.kt
│   │   │   │   │   │   └── MandiPriceEntity.kt
│   │   │   │   │   ├── model/         # Data models
│   │   │   │   │   │   ├── FarmerProduct.kt
│   │   │   │   │   │   ├── MandiApiResponse.kt
│   │   │   │   │   │   ├── MandiPrice.kt
│   │   │   │   │   │   ├── Product.kt
│   │   │   │   │   │   └── WeatherModels.kt
│   │   │   │   │   ├── repository/    # Data layer
│   │   │   │   │   │   ├── AuthRepository.kt
│   │   │   │   │   │   ├── MandiPricesRepository.kt
│   │   │   │   │   │   ├── MarketplaceRepository.kt
│   │   │   │   │   │   ├── ProfileRepository.kt
│   │   │   │   │   │   └── WeatherRepository.kt
│   │   │   │   │   └── NewsApiService.kt
│   │   │   │   ├── di/                # Dependency Injection
│   │   │   │   │   ├── FirebaseModule.kt
│   │   │   │   │   └── NetworkModule.kt
│   │   │   │   ├── navigation/        # Navigation
│   │   │   │   │   ├── MittiSeNavigation.kt
│   │   │   │   │   └── Screen.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── apmc/          # APMC module
│   │   │   │   │   │   ├── ApmcViewModel.kt
│   │   │   │   │   │   └── MandiPriceCard.kt
│   │   │   │   │   ├── language/      # Language module
│   │   │   │   │   │   └── LanguageViewModel.kt
│   │   │   │   │   ├── marketplace/   # Marketplace module
│   │   │   │   │   │   └── MarketplaceViewModel.kt
│   │   │   │   │   ├── screens/       # UI Screens
│   │   │   │   │   │   ├── AboutScreen.kt
│   │   │   │   │   │   ├── AuthScreens.kt
│   │   │   │   │   │   ├── DashboardScreen.kt
│   │   │   │   │   │   ├── EquipmentScreen.kt
│   │   │   │   │   │   ├── FeedbackScreen.kt
│   │   │   │   │   │   ├── FunctionalScreens.kt
│   │   │   │   │   │   ├── HelpScreen.kt
│   │   │   │   │   │   ├── LanguageScreen.kt
│   │   │   │   │   │   ├── MarketplaceScreen.kt
│   │   │   │   │   │   ├── PesticidesScreen.kt
│   │   │   │   │   │   ├── PlaceholderScreens.kt
│   │   │   │   │   │   └── SeedsScreen.kt
│   │   │   │   │   ├── theme/         # Material Design 3
│   │   │   │   │   │   ├── Animations.kt
│   │   │   │   │   │   ├── EnhancedUI.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── viewmodels/    # ViewModels
│   │   │   │   │   │   ├── AuthViewModel.kt
│   │   │   │   │   │   ├── ChatbotViewModel.kt
│   │   │   │   │   │   ├── NewsViewModel.kt
│   │   │   │   │   │   └── ProfileViewModel.kt
│   │   │   │   │   └── weather/       # Weather module
│   │   │   │   │       └── WeatherViewModel.kt
│   │   │   │   ├── util/              # Utilities
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── GoogleSignInConfig.kt
│   │   │   │   │   ├── GoogleSignInHelper.kt
│   │   │   │   │   ├── GoogleSignInState.kt
│   │   │   │   │   ├── LocaleHelper.kt
│   │   │   │   │   ├── NavigationUtil.kt
│   │   │   │   │   ├── NetworkUtil.kt
│   │   │   │   │   ├── Transition3D.kt
│   │   │   │   │   └── ValidationUtil.kt
│   │   │   │   ├── MainActivityCompose.kt
│   │   │   │   └── MittiSeApplication.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/          # Icons & Images
│   │   │   │   ├── layout/            # XML layouts
│   │   │   │   ├── mipmap-*/          # App icons
│   │   │   │   ├── raw/               # Raw resources
│   │   │   │   ├── values/            # Default strings
│   │   │   │   ├── values-hi/         # Hindi strings
│   │   │   │   ├── values-mr/         # Marathi strings
│   │   │   │   ├── values-ta/         # Tamil strings
│   │   │   │   ├── values-te/         # Telugu strings
│   │   │   │   ├── values-ml/         # Malayalam strings
│   │   │   │   └── values-night/      # Dark theme
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/               # Instrumented tests
│   │   └── test/                      # Unit tests
│   ├── build.gradle.kts
│   └── google-services.json
├── gradle/
│   └── libs.versions.toml             # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── CHATBOT_INTEGRATION.md
├── GOOGLE_SIGN_IN_SETUP.md
├── NEWS_API_INTEGRATION.md
└── README.md
```

---

## 📚 Dependencies

### Core Dependencies
```kotlin
// Android Core
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")

// Jetpack Compose
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
implementation("androidx.activity:activity-compose:1.8.2")
implementation("androidx.navigation:navigation-compose:2.7.7")

// ViewModel & LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Hilt Dependency Injection
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-android-compiler:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// Retrofit & Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

// Image Loading
implementation("com.github.bumptech.glide:glide:4.16.0")
implementation("io.coil-kt:coil-compose:2.5.0")

// Lottie Animations
implementation("com.airbnb.android:lottie:6.4.0")
implementation("com.airbnb.android:lottie-compose:6.4.0")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
implementation("com.google.firebase:firebase-analytics")
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")

// Google Sign-In
implementation("com.google.android.gms:play-services-auth:20.7.0")

// Testing
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```

---

## 🔐 Permissions

Required permissions in `AndroidManifest.xml`:

```xml
<!-- Internet & Network -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Location (for weather) -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Camera (for product images) -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="false" />

<!-- Storage (for image upload) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Microphone (for voice input) -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

---

## 🌍 Language Support

MittiSe supports 6+ Indian languages:

| Language | Code | Resources |
|----------|------|-----------|
| English | `en` | `values/` |
| Hindi | `hi` | `values-hi/` |
| Marathi | `mr` | `values-mr/` |
| Tamil | `ta` | `values-ta/` |
| Telugu | `te` | `values-te/` |
| Malayalam | `ml` | `values-ml/` |

### Language Switching
Users can change language from:
- Settings → Language
- Language screen in navigation drawer

The app automatically:
- Saves language preference
- Applies locale changes
- Restarts with selected language

Implementation: `LocaleHelper.kt` & `LanguageViewModel.kt`

---


## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Guidelines:
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Add comments for complex logic
- Update documentation
- Test thoroughly before submitting

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 

**Developer**: Dhiraj Autade  
**GitHub**: [@Dhiraj-Autade](https://github.com/Dhiraj-Autade)  
**Repository**: [MittiSe](https://github.com/Dhiraj-Autade/MittiSe)

---

## 🙏 Acknowledgments

- **OpenWeatherMap** - Weather data API
- **Government of India** - Mandi prices data
- **News API** - Agricultural news
- **Google Gemini AI** - Chatbot intelligence
- **Firebase** - Backend services
- **Android Community** - Open source libraries

---

## 📞 Support

For support, email: [autadedhiraj33@gmail.com]  
Or open an issue on [GitHub Issues](https://github.com/Dhiraj-Autade/MittiSe/issues)

---

## 🔮 Future Enhancements

### **Planned Features**

#### **Social Networking Features**
- **Social Feed**: Community-driven content sharing
  - Post creation with text, images, and videos
  - Farmer-to-farmer communication platform
  - Success stories and farming experiences
  - Crop-specific discussion threads
- **Community Interactions**:
  - Like and comment system
  - Share posts with other farmers
  - Follow other farmers
  - Private messaging
  - Group chats for farming communities
- **Expert Connect**: Direct interaction with agricultural experts
- **Knowledge Sharing**: Best practices and tips sharing

#### **Government Schemes & Support**
- **Schemes Database**: Comprehensive list of Indian agricultural schemes
  - PM-KISAN scheme details
  - Soil Health Card scheme
  - Pradhan Mantri Fasal Bima Yojana
  - State-specific schemes
- **Eligibility Checker**: Automatic scheme eligibility verification
- **Application Assistance**: Step-by-step guidance for scheme applications
- **Notifications**: Alerts for new schemes and deadlines

#### **Educational Content**
- **Articles & Guides**: 
  - Farming best practices
  - Crop-specific cultivation guides
  - Pest management techniques
  - Organic farming methods
  - Modern farming technologies
- **Video Tutorials**: 
  - Step-by-step farming techniques
  - Equipment usage guides
  - Success stories from fellow farmers
- **Expert Webinars**: Live sessions with agricultural experts
- **Seasonal Advisories**: Time-sensitive farming recommendations

#### **Enhanced User Experience**
- **Dark Mode Support**: 
  - System-wide dark theme
  - Automatic theme switching
  - OLED-optimized dark colors
  - Reduced eye strain for night usage
- **Help & Support System**:
  - In-app help documentation
  - FAQ section
  - Live chat support
  - Video tutorials
  - Troubleshooting guides
- **Feedback System**: 
  - User feedback collection
  - Feature requests
  - Bug reporting
  - App rating and reviews

#### **Multi-language Expansion**
- Current support: English, Hindi, Marathi, Tamil, Telugu, Malayalam
- Planned additions:
  - Kannada
  - Gujarati
  - Bengali
  - Punjabi
  - Odia
  - Assamese
- **Voice Support**: Regional language voice commands and responses

#### **Advanced Image Processing**
- **Glide Integration**:
  - Efficient image loading and caching
  - Memory optimization
  - Placeholder and error handling
  - Image transformations
- **Coil for Compose**:
  - Native Compose image loading
  - Async image loading
  - Seamless integration with Material Design 3
  - SVG support
- **Soil Testing Analysis**: Image-based soil quality assessment

### **Technology Upgrades**

#### **Offline Mode**
- Local data caching for offline access
- Sync when internet is available
- Offline weather forecasts
- Cached market prices
- Downloadable articles and guides

#### **AI & Machine Learning**
- **Image-based Crop Disease Detection**:
  - Upload crop photos for instant diagnosis
  - AI-powered disease identification
  - Treatment recommendations
  - Pest identification
- **Crop Yield Prediction**: ML models for yield forecasting
- **Smart Recommendations**: Personalized farming advice based on user data
- **Chatbot Improvements**: 
  - Multi-language voice support
  - Context-aware conversations
  - Historical chat memory
  - Image recognition in chat

#### **Push Notifications**
- Price alerts for commodities
- Weather warnings and advisories
- New scheme announcements
- Community post notifications
- Market trend alerts
- Crop calendar reminders

#### **Advanced Marketplace Features**
- **Equipment Rental**: Rent farming equipment from other farmers
- **Bulk Orders**: Wholesale purchasing options
- **Price Negotiation**: Built-in negotiation system
- **Verified Sellers**: Seller rating and verification
- **Payment Integration**: UPI, credit/debit card support
- **Order Tracking**: Real-time order status

#### **Finance & Loan Management**
- Loan calculator
- Interest rate comparisons
- Loan application assistance
- Credit score checker
- Insurance information
- Investment planning

#### **Livestock Management**
- Cattle/livestock tracking
- Health monitoring
- Vaccination reminders
- Breeding records
- Feed management

#### **IoT Integration**
- Smart farming device connectivity
- Soil moisture sensors
- Weather stations
- Automated irrigation control
- Remote monitoring dashboards

#### **Blockchain for Transparency**
- Supply chain tracking
- Product authenticity verification
- Fair price guarantee
- Direct farmer-to-consumer platform

### **User Interface Enhancements**
- 3D transitions and animations
- Voice-controlled navigation
- Gesture-based controls
- Customizable dashboard
- Widget support for home screen
- Tablet and foldable device optimization

---

## 🗺️ Development Roadmap

### **Phase 1: Foundation** (Completed ✅)
- ✅ Core authentication system
- ✅ Basic UI with Jetpack Compose
- ✅ Weather integration
- ✅ APMC/Mandi prices
- ✅ AI Chatbot
- ✅ Marketplace basics
- ✅ Multi-language support

### **Phase 2: Enhancement** (Current Phase 🚧)
- 🚧 Social networking features
- 🚧 Government schemes database
- 🚧 Educational content library
- 🚧 Advanced image processing
- 🚧 Dark mode optimization
- 🚧 Offline mode implementation

### **Phase 3: Intelligence** (Upcoming 🔜)
- 🔜 AI crop disease detection
- 🔜 ML yield prediction
- 🔜 Smart recommendations
- 🔜 IoT device integration
- 🔜 Advanced analytics
- 🔜 Blockchain integration

### **Phase 4: Scale** (Future 🔮)
- 🔮 Equipment rental marketplace
- 🔮 Finance & loan services
- 🔮 Livestock management
- 🔮 Video conferencing with experts
- 🔮 Drone integration for field monitoring
- 🔮 Export opportunities platform

---

## 🔧 Troubleshooting

### Common Issues:

**1. Google Sign-In not working**
- Verify Web Client ID in `GoogleSignInConfig.kt`
- Check SHA-1 fingerprint in Firebase
- Ensure Google Sign-In is enabled in Firebase Console

**2. Weather not loading**
- Check OpenWeatherMap API key
- Verify internet connection
- Enable location permissions

**3. Mandi prices not showing**
- Check internet connection
- Verify Government API is accessible
- Check API rate limits

**4. Chatbot not responding**
- Verify Gemini API key
- Check internet connection
- Review API quota limits

**5. Build errors**
- Clean project: `Build → Clean Project`
- Sync Gradle: `File → Sync Project with Gradle Files`
- Invalidate caches: `File → Invalidate Caches / Restart`

---

## 📊 App Statistics

- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Compile SDK**: API 35
- **Language**: Kotlin 1.9.23
- **Gradle**: 8.8.0
- **Architecture**: MVVM
- **UI**: Jetpack Compose

---

<div align="center">

**Made with ❤️ for Indian Farmers**



</div>
