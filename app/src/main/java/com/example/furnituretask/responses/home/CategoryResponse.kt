package com.example.furnituretask.responses.home

data class CategoryResponse(
    val `data`: List<Category>,
    val message: String,
    val status: Boolean
)