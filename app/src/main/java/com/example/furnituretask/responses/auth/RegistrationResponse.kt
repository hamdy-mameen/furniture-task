package com.example.furnituretask.responses.auth

data class RegistrationResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)