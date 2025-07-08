package com.example.mittise.data.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException

/**
 * AI Service interface for chatbot functionality
 * Can be implemented with OpenAI, Gemini, or other AI providers
 */
interface AIService {
    suspend fun getResponse(prompt: String): String
    suspend fun getStreamingResponse(prompt: String): Flow<String>
}

/**
 * Mock AI Service for development and testing
 * Replace this with actual OpenAI or Gemini implementation
 */
class MockAIService : AIService {
    
    private val farmingResponses = listOf(
        "Based on current weather conditions, I recommend planting drought-resistant crops like millets and pulses. These crops require less water and are more suitable for the current climate.",
        
        "For pest control in tomatoes, you can use neem oil spray or introduce beneficial insects like ladybugs. Neem oil is a natural pesticide that's safe for your crops and the environment.",
        
        "The current market prices for wheat are ₹2,100 per quintal. Prices are expected to remain stable this week. Consider selling your produce when prices are favorable.",
        
        "For better soil health, consider crop rotation and adding organic matter like compost or green manure. This will improve soil fertility and reduce pest problems naturally.",
        
        "The weather forecast shows light rain in the next 3 days. It's a good time for sowing seeds as the soil will be moist and favorable for germination.",
        
        "To increase crop yield, ensure proper irrigation, use quality seeds, apply balanced fertilizers, and practice timely weeding. Regular monitoring is also crucial.",
        
        "For organic farming, focus on soil health, use natural pest control methods, practice crop rotation, and maintain biodiversity in your farm ecosystem.",
        
        "The best time to harvest wheat is when the grain is hard and the straw is golden yellow. This usually occurs 7-10 days after the grain has reached physiological maturity.",
        
        "For vegetable farming, ensure proper spacing between plants, use organic mulch to retain soil moisture, and practice companion planting to naturally deter pests.",
        
        "To improve soil fertility, consider using green manure crops like dhaincha or sunn hemp. These crops add nitrogen to the soil and improve soil structure."
    )
    
    private val contextResponses = mapOf(
        "weather" to listOf(
            "The current weather is favorable for most crops. Light rain is expected in the next few days, which is good for newly planted seeds.",
            "High temperatures and low humidity may stress your crops. Consider increasing irrigation frequency and providing shade where possible.",
            "The weather forecast indicates a cold spell. Protect your crops with appropriate coverings and avoid planting sensitive crops during this period."
        ),
        "pest" to listOf(
            "For natural pest control, consider using neem oil, garlic spray, or introducing beneficial insects like ladybugs and lacewings.",
            "Regular monitoring of your crops is essential for early pest detection. Look for signs like holes in leaves, yellowing, or stunted growth.",
            "Crop rotation and intercropping can help reduce pest populations naturally by disrupting their life cycles."
        ),
        "fertilizer" to listOf(
            "Organic fertilizers like compost, vermicompost, and farmyard manure are excellent for soil health and crop growth.",
            "Apply fertilizers based on soil test results. Over-fertilization can harm your crops and the environment.",
            "Consider using bio-fertilizers like Rhizobium for legumes and Azotobacter for cereals to improve nitrogen fixation."
        ),
        "market" to listOf(
            "Current market prices are favorable for most crops. Consider selling your produce when prices are high.",
            "Market demand for organic produce is increasing. Consider transitioning to organic farming for better profits.",
            "Connect with local APMC markets for better price discovery and direct selling opportunities."
        )
    )
    
    override suspend fun getResponse(prompt: String): String {
        // Simulate API delay
        delay(1500)
        
        // Simple keyword-based response selection
        val lowerPrompt = prompt.lowercase()
        
        return when {
            lowerPrompt.contains("weather") -> contextResponses["weather"]?.random() ?: farmingResponses.random()
            lowerPrompt.contains("pest") || lowerPrompt.contains("insect") -> contextResponses["pest"]?.random() ?: farmingResponses.random()
            lowerPrompt.contains("fertilizer") || lowerPrompt.contains("nutrient") -> contextResponses["fertilizer"]?.random() ?: farmingResponses.random()
            lowerPrompt.contains("market") || lowerPrompt.contains("price") -> contextResponses["market"]?.random() ?: farmingResponses.random()
            else -> farmingResponses.random()
        }
    }
    
    override suspend fun getStreamingResponse(prompt: String): Flow<String> = flow {
        val response = getResponse(prompt)
        val words = response.split(" ")
        
        words.forEach { word ->
            emit(word + " ")
            delay(100) // Simulate streaming delay
        }
    }
}

/**
 * OpenAI Service implementation (placeholder)
 * TODO: Implement with actual OpenAI API
 */
class OpenAIService(private val apiKey: String) : AIService {
    override suspend fun getResponse(prompt: String): String {
        // TODO: Implement OpenAI API call
        // Example implementation:
        // val client = OkHttpClient()
        // val request = Request.Builder()
        //     .url("https://api.openai.com/v1/chat/completions")
        //     .addHeader("Authorization", "Bearer $apiKey")
        //     .post(RequestBody.create(MediaType.parse("application/json"), 
        //         """{"model": "gpt-3.5-turbo", "messages": [{"role": "user", "content": "$prompt"}]}"""))
        //     .build()
        // 
        // val response = client.newCall(request).execute()
        // return parseOpenAIResponse(response.body?.string())
        
        // For now, return mock response
        return MockAIService().getResponse(prompt)
    }
    
    override suspend fun getStreamingResponse(prompt: String): Flow<String> {
        // TODO: Implement OpenAI streaming API
        return MockAIService().getStreamingResponse(prompt)
    }
}

/**
 * Gemini Service implementation with real API integration
 */
class GeminiService(private val apiKey: String) : AIService {
    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()
    
    override suspend fun getResponse(prompt: String): String {
        return try {
            val systemPrompt = """
                You are an AI farming assistant for Indian farmers. Provide helpful, practical advice about agriculture, crops, weather, pest control, and farming techniques. 
                Keep responses concise, actionable, and relevant to Indian farming conditions. 
                Focus on organic and sustainable farming practices when possible.
                If the question is not related to agriculture or farming, politely redirect the conversation back to farming topics.
                Always respond in a helpful and friendly manner.
            """.trimIndent()
            
            // Build JSON using Android-compatible JSONObject/JSONArray
            val partsArray = JSONArray().apply {
                put(JSONObject().put("text", "$systemPrompt\n\nUser question: $prompt"))
            }
            val contentsArray = JSONArray().apply {
                put(JSONObject().put("parts", partsArray))
            }
            val generationConfig = JSONObject()
                .put("temperature", 0.7)
                .put("topK", 40)
                .put("topP", 0.95)
                .put("maxOutputTokens", 1000)
            val requestBody = JSONObject()
                .put("contents", contentsArray)
                .put("generationConfig", generationConfig)
                .toString()
            
            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$apiKey")
                .post(requestBody.toRequestBody(mediaType))
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (response.isSuccessful && responseBody != null) {
                parseGeminiResponse(responseBody)
            } else {
                android.util.Log.e("GeminiService", "API error: ${response.code} ${response.message} body: $responseBody")
                "Sorry, I'm having trouble connecting to the AI service right now. Please try again later."
            }
        } catch (e: Exception) {
            android.util.Log.e("GeminiService", "Exception: ${e.message}", e)
            "Sorry, I encountered an error while processing your request. Please try again."
        }
    }
    
    override suspend fun getStreamingResponse(prompt: String): Flow<String> = flow {
        val response = getResponse(prompt)
        val words = response.split(" ")
        words.forEach { word ->
            emit(word + " ")
            delay(100)
        }
    }
    
    private fun parseGeminiResponse(responseBody: String): String {
        return try {
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.getJSONArray("candidates")
            if (candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                if (parts.length() > 0) {
                    val part = parts.getJSONObject(0)
                    part.getString("text")
                } else {
                    "Sorry, I couldn't generate a response."
                }
            } else {
                "Sorry, I couldn't generate a response."
            }
        } catch (e: Exception) {
            android.util.Log.e("GeminiService", "Parse error: ${e.message}", e)
            "Sorry, I couldn't parse the response properly."
        }
    }
    
    suspend fun testConnection(): String {
        return getResponse("Hello, can you help me with farming advice?")
    }
} 