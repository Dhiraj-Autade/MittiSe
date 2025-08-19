package com.example.mittise.util

object ValidationUtil {
    
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    
    fun isValidPhone(phone: String): Boolean {
        // Indian phone number validation
        val phonePattern = "^[+]?[0-9]{10,13}$"
        return phone.matches(phonePattern.toRegex())
    }
    
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2 && name.all { it.isLetter() || it.isWhitespace() }
    }
    
    fun validateProfileData(
        firstName: String,
        lastName: String,
        phone: String,
        location: String
    ): ValidationResult {
        val errors = mutableListOf<String>()
        
        if (!isValidName(firstName)) {
            errors.add("First name must be at least 2 characters and contain only letters")
        }
        
        if (!isValidName(lastName)) {
            errors.add("Last name must be at least 2 characters and contain only letters")
        }
        
        if (phone.isNotBlank() && !isValidPhone(phone)) {
            errors.add("Please enter a valid phone number")
        }
        
        if (location.isBlank()) {
            errors.add("Location is required")
        }
        
        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<String>
    )
}
