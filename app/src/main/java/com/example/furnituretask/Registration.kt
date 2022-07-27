package com.example.furnituretask

data class Registration(
    val name:String,
    val email:String,
    val phone:String,
    val address:String,
    val password:String,
    val password_confirmation: String
)
