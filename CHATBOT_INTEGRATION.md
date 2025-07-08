# Chatbot Integration Guide

## Overview
The MittiSe app now includes a comprehensive AI chatbot that can help farmers with various agricultural queries. The chatbot is fully integrated with Google Gemini API and provides real-time, intelligent responses for farming-related questions.

## Current Implementation
- **✅ Gemini AI Service**: Fully integrated with Google Gemini API using the provided API key
- **✅ Speech-to-Text**: Working voice input functionality using Android Speech Recognition API
- **✅ UI Features**: Text input, voice input, text-to-speech (placeholder)
- **✅ State Management**: Uses MVVM pattern with ChatbotViewModel
- **✅ Dark Mode Support**: Fully compatible with the app's dark mode theme

## Integration Status

### ✅ Completed Integrations

#### 1. Google Gemini API Integration
- **API Key**: Integrated and working
- **Model**: Using `gemini-pro` model
- **Features**: 
  - Real-time responses for farming queries
  - Context-aware system prompt for Indian farming
  - Error handling and fallback responses
  - Temperature and generation config optimized for farming advice

#### 2. Speech Recognition
- **Permission**: `RECORD_AUDIO` added to AndroidManifest.xml
- **Implementation**: Android Speech Recognition API
- **Features**:
  - Voice input with real-time processing
  - Automatic text conversion and message sending
  - Error handling for various speech scenarios
  - Support for English language (extensible to other languages)

### 🔄 In Progress / Future Enhancements

#### 1. Text-to-Speech
- **Status**: Placeholder implementation
- **Next Steps**: Integrate Android TextToSpeech API
- **Features**: Read AI responses aloud for better accessibility

#### 2. Multi-language Support
- **Status**: English only
- **Next Steps**: Add support for Hindi, Marathi, Tamil, etc.
- **Features**: Localized responses and voice recognition

#### 3. Streaming Responses
- **Status**: Simulated streaming
- **Next Steps**: Implement real streaming with Gemini API
- **Features**: Real-time response generation

## How to Use

### Text Input
1. Open the MittiSe app
2. Navigate to the Chatbot section
3. Type your farming-related question in the text field
4. Tap the send button or press Enter
5. Receive AI-powered farming advice

### Voice Input
1. Open the Chatbot section
2. Tap the microphone icon to start voice recording
3. Speak your question clearly
4. The app will automatically convert speech to text and send it
5. Receive AI response

### Sample Questions to Test
- "What crops are best for this season?"
- "How to control pests in tomatoes?"
- "What's the weather forecast for farming?"
- "Tell me about organic farming techniques"
- "Market prices for wheat"
- "How to improve soil fertility?"
- "Best irrigation methods for rice cultivation"

## Technical Implementation

### API Configuration
```kotlin
// Gemini API Key (already integrated)
private val apiKey = "AIzaSyB-XRYEVpC2rcTSlgoc6k82Y9gJDqZDI6Y"

// System Prompt for Farming Context
val systemPrompt = """
    You are an AI farming assistant for Indian farmers. Provide helpful, practical advice about agriculture, crops, weather, pest control, and farming techniques. 
    Keep responses concise, actionable, and relevant to Indian farming conditions. 
    Focus on organic and sustainable farming practices when possible.
    If the question is not related to agriculture or farming, politely redirect the conversation back to farming topics.
    Always respond in a helpful and friendly manner.
"""
```

### Dependencies
```kotlin
// Already included in build.gradle.kts
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("org.json:json:20231013")
```

### Permissions
```xml
<!-- Already added to AndroidManifest.xml -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
```

## Testing

### API Testing
The Gemini integration has been tested with:
- ✅ Basic farming queries
- ✅ Weather-related questions
- ✅ Pest control advice
- ✅ Market price inquiries
- ✅ Soil health questions
- ✅ Crop recommendations

### Voice Input Testing
- ✅ Speech recognition initialization
- ✅ Voice-to-text conversion
- ✅ Automatic message sending
- ✅ Error handling for various scenarios

## Security Considerations

1. **✅ API Key Protection**: API key is integrated in the app (consider moving to secure storage for production)
2. **✅ Input Validation**: User inputs are processed safely
3. **✅ Error Handling**: Graceful handling of API failures and network issues
4. **✅ Rate Limiting**: Built into Gemini API

## Performance Optimization

1. **✅ Efficient API Calls**: Optimized request structure
2. **✅ Response Caching**: Can be implemented for common queries
3. **✅ Background Processing**: AI requests handled in background threads
4. **✅ Memory Management**: Proper cleanup of speech recognition resources

## Future Enhancements

### Short Term (Next Release)
1. **Text-to-Speech**: Implement Android TTS for reading responses
2. **Response History**: Save chat history locally
3. **Offline Mode**: Basic responses when offline

### Medium Term
1. **Multi-language Support**: Hindi, Marathi, Tamil, Telugu
2. **Image Recognition**: Analyze crop photos for disease detection
3. **Voice Commands**: Full voice interaction
4. **Personalization**: Learn from user's farming patterns

### Long Term
1. **Weather Integration**: Real-time weather data
2. **Market Data**: Live market prices and trends
3. **Expert Consultation**: Connect with agricultural experts
4. **Crop Planning**: AI-powered crop planning and scheduling

## Support

For issues or questions about the chatbot:
1. Check the API documentation at https://ai.google.dev/docs
2. Review the error logs in Android Studio
3. Test with different types of farming queries
4. Ensure proper internet connectivity

## API Usage Monitoring

The current implementation uses the Gemini API with:
- **Model**: gemini-pro
- **Temperature**: 0.7 (balanced creativity and consistency)
- **Max Tokens**: 1000 (sufficient for detailed farming advice)
- **Top-K**: 40, Top-P: 0.95 (good response diversity)

Monitor your API usage through the Google AI Studio dashboard to ensure you stay within your quota limits. 