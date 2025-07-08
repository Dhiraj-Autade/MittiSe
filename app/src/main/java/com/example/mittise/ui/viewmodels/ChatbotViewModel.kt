package com.example.mittise.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.api.AIService
import com.example.mittise.data.api.MockAIService
import com.example.mittise.data.api.GeminiService
import com.example.mittise.ui.screens.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.os.Bundle

class ChatbotViewModel : ViewModel() {
    
    // Use real Gemini API with the provided key
    private val aiService: AIService = GeminiService("AIzaSyB-XRYEVpC2rcTSlgoc6k82Y9gJDqZDI6Y")
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()
    
    private var speechRecognizer: SpeechRecognizer? = null
    private var context: Context? = null
    
    fun setContext(context: Context) {
        this.context = context
        initializeSpeechRecognizer()
    }
    
    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context!!)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context!!)
            speechRecognizer?.setRecognitionListener(object : android.speech.RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    // Speech recognition is ready
                }
                
                override fun onBeginningOfSpeech() {
                    // User started speaking
                }
                
                override fun onRmsChanged(rmsdB: Float) {
                    // Audio level changed
                }
                
                override fun onBufferReceived(buffer: ByteArray?) {
                    // Audio buffer received
                }
                
                override fun onEndOfSpeech() {
                    // User stopped speaking
                }
                
                override fun onError(error: Int) {
                    _isListening.value = false
                    // Handle speech recognition errors
                    when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            // No speech was recognized
                        }
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                            // Speech input timed out
                        }
                        SpeechRecognizer.ERROR_NETWORK -> {
                            // Network error
                        }
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {
                            // Network timeout
                        }
                        SpeechRecognizer.ERROR_AUDIO -> {
                            // Audio recording error
                        }
                        SpeechRecognizer.ERROR_SERVER -> {
                            // Server error
                        }
                        SpeechRecognizer.ERROR_CLIENT -> {
                            // Client error
                        }
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                            // Permission denied
                        }
                    }
                }
                
                override fun onResults(results: Bundle?) {
                    _isListening.value = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val recognizedText = matches[0]
                        // Send the recognized text as a message
                        sendMessage(recognizedText)
                    }
                }
                
                override fun onPartialResults(partialResults: Bundle?) {
                    // Partial results received
                }
                
                override fun onEvent(eventType: Int, params: Bundle?) {
                    // Speech recognition event
                }
            })
        }
    }
    
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        // Add user message
        val userMessage = ChatMessage(
            text = text,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        _messages.value = _messages.value + userMessage
        
        // Show typing indicator
        _isTyping.value = true
        
        // Get AI response
        viewModelScope.launch {
            try {
                val response = aiService.getResponse(text)
                
                // Add AI response
                val aiMessage = ChatMessage(
                    text = response,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + aiMessage
            } catch (e: Exception) {
                // Handle error
                val errorMessage = ChatMessage(
                    text = "Sorry, I'm having trouble responding right now. Please try again later.",
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + errorMessage
            } finally {
                _isTyping.value = false
            }
        }
    }
    
    fun toggleVoiceInput() {
        if (_isListening.value) {
            stopSpeechRecognition()
        } else {
            startSpeechRecognition()
        }
    }
    
    fun speakMessage(message: String) {
        _isSpeaking.value = true
        
        viewModelScope.launch {
            try {
                // TODO: Implement text-to-speech
                // textToSpeech.speak(message)
                delay(2000) // Simulate TTS duration
            } finally {
                _isSpeaking.value = false
            }
        }
    }
    
    fun clearChat() {
        _messages.value = emptyList()
    }
    
    private fun startSpeechRecognition() {
        if (context == null) return
        
        if (speechRecognizer == null) {
            initializeSpeechRecognizer()
        }
        
        _isListening.value = true
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // You can change this to support other languages
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        
        speechRecognizer?.startListening(intent)
    }
    
    private fun stopSpeechRecognition() {
        _isListening.value = false
        speechRecognizer?.stopListening()
    }
    
    // Method to set AI service (for testing or switching providers)
    fun setAIService(service: AIService) {
        // This would be used when switching from mock to real AI service
        // For now, we'll keep the mock service
    }
    
    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
} 