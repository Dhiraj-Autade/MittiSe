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
    
    // Use real Gemini API with the provided key and latest model
    private val aiService: AIService = GeminiService("AIzaSyD_TWERer7pOCqxWPV0fNpstLON2u1KrAk")
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()
    
    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()
    
    private var speechRecognizer: SpeechRecognizer? = null
    private var context: Context? = null
    
    fun setContext(context: Context) {
        this.context = context
        initializeSpeechRecognizer()
    }
    
    private fun initializeSpeechRecognizer() {
        try {
            if (context == null) {
                android.util.Log.e("ChatbotViewModel", "Context is null, cannot initialize speech recognizer")
                return
            }
            
            if (!SpeechRecognizer.isRecognitionAvailable(context!!)) {
                android.util.Log.e("ChatbotViewModel", "Speech recognition is not available on this device")
                return
            }
            
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context!!)
            speechRecognizer?.setRecognitionListener(object : android.speech.RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    android.util.Log.d("ChatbotViewModel", "Speech recognition ready")
                }
                
                override fun onBeginningOfSpeech() {
                    android.util.Log.d("ChatbotViewModel", "Speech beginning")
                }
                
                override fun onRmsChanged(rmsdB: Float) {
                    // Audio level changed
                }
                
                override fun onBufferReceived(buffer: ByteArray?) {
                    // Audio buffer received
                }
                
                override fun onEndOfSpeech() {
                    android.util.Log.d("ChatbotViewModel", "Speech ended")
                }
                
                override fun onError(error: Int) {
                    _isListening.value = false
                    android.util.Log.e("ChatbotViewModel", "Speech recognition error: $error")
                    
                    // Handle speech recognition errors
                    when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> {
                            android.util.Log.e("ChatbotViewModel", "No speech was recognized")
                            // Retry after a short delay
                            retrySpeechRecognition()
                        }
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                            android.util.Log.e("ChatbotViewModel", "Speech input timed out")
                            // Retry after a short delay
                            retrySpeechRecognition()
                        }
                        SpeechRecognizer.ERROR_NETWORK -> {
                            android.util.Log.e("ChatbotViewModel", "Network error")
                            // Retry after a longer delay for network issues
                            retrySpeechRecognition(delay = 2000)
                        }
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {
                            android.util.Log.e("ChatbotViewModel", "Network timeout")
                            // Retry after a longer delay for network issues
                            retrySpeechRecognition(delay = 2000)
                        }
                        SpeechRecognizer.ERROR_AUDIO -> {
                            android.util.Log.e("ChatbotViewModel", "Audio recording error")
                            // Retry after a short delay
                            retrySpeechRecognition()
                        }
                        SpeechRecognizer.ERROR_SERVER -> {
                            android.util.Log.e("ChatbotViewModel", "Server error")
                            // Retry after a longer delay for server issues
                            retrySpeechRecognition(delay = 3000)
                        }
                        SpeechRecognizer.ERROR_CLIENT -> {
                            android.util.Log.e("ChatbotViewModel", "Client error")
                            // Reinitialize speech recognizer
                            reinitializeSpeechRecognizer()
                        }
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                            android.util.Log.e("ChatbotViewModel", "Permission denied")
                        }
                    }
                }
                
                override fun onResults(results: Bundle?) {
                    _isListening.value = false
                    android.util.Log.d("ChatbotViewModel", "Speech recognition results received")
                    
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val recognizedText = matches[0]
                        android.util.Log.d("ChatbotViewModel", "Recognized text: $recognizedText")
                        // Set the recognized text in the input field instead of sending immediately
                        _recognizedText.value = recognizedText
                    } else {
                        android.util.Log.e("ChatbotViewModel", "No recognition results")
                        retrySpeechRecognition()
                    }
                }
                
                override fun onPartialResults(partialResults: Bundle?) {
                    // Partial results received
                }
                
                override fun onEvent(eventType: Int, params: Bundle?) {
                    // Speech recognition event
                }
            })
            
            android.util.Log.d("ChatbotViewModel", "Speech recognizer initialized successfully")
        } catch (e: Exception) {
            android.util.Log.e("ChatbotViewModel", "Error initializing speech recognizer: ${e.message}", e)
        }
    }
    
    private fun retrySpeechRecognition(delay: Long = 1000) {
        viewModelScope.launch {
            delay(delay)
            if (_isListening.value) {
                android.util.Log.d("ChatbotViewModel", "Retrying speech recognition...")
                startSpeechRecognition()
            }
        }
    }
    
    private fun reinitializeSpeechRecognizer() {
        viewModelScope.launch {
            try {
                speechRecognizer?.destroy()
                speechRecognizer = null
                delay(500)
                initializeSpeechRecognizer()
                if (_isListening.value) {
                    startSpeechRecognition()
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatbotViewModel", "Error reinitializing speech recognizer: ${e.message}", e)
            }
        }
    }
    
    fun clearRecognizedText() {
        _recognizedText.value = ""
    }
    
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        // Clear recognized text
        _recognizedText.value = ""
        
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
        try {
            if (_isListening.value) {
                android.util.Log.d("ChatbotViewModel", "Stopping speech recognition")
                stopSpeechRecognition()
            } else {
                android.util.Log.d("ChatbotViewModel", "Starting speech recognition")
                startSpeechRecognition()
            }
        } catch (e: Exception) {
            android.util.Log.e("ChatbotViewModel", "Error toggling voice input: ${e.message}", e)
            _isListening.value = false
        }
    }
    
    /**
     * Check if microphone permission is granted
     */
    fun isMicrophonePermissionGranted(): Boolean {
        return context?.let { ctx ->
            android.Manifest.permission.RECORD_AUDIO.let { permission ->
                android.content.pm.PackageManager.PERMISSION_GRANTED == 
                ctx.checkSelfPermission(permission)
            }
        } ?: false
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
        try {
            if (context == null) {
                android.util.Log.e("ChatbotViewModel", "Context is null, cannot start speech recognition")
                return
            }
            
            if (speechRecognizer == null) {
                android.util.Log.d("ChatbotViewModel", "Speech recognizer is null, initializing...")
                initializeSpeechRecognizer()
            }
            
            if (speechRecognizer == null) {
                android.util.Log.e("ChatbotViewModel", "Failed to initialize speech recognizer")
                return
            }
            
            _isListening.value = true
            android.util.Log.d("ChatbotViewModel", "Starting speech recognition...")
            
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // You can change this to support other languages
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }
            
            speechRecognizer?.startListening(intent)
            android.util.Log.d("ChatbotViewModel", "Speech recognition started successfully")
            
        } catch (e: Exception) {
            android.util.Log.e("ChatbotViewModel", "Error starting speech recognition: ${e.message}", e)
            _isListening.value = false
        }
    }
    
    private fun stopSpeechRecognition() {
        _isListening.value = false
        speechRecognizer?.stopListening()
    }
    
    // Method to set AI service (for testing or switching providers)
    fun setAIService(_service: AIService) {
        // This would be used when switching from mock to real AI service
        // For now, we'll keep the mock service
    }
    
    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
} 