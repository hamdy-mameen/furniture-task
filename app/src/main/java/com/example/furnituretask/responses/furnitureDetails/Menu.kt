package com.example.furnituretask.responses.furnitureDetails

data class Menu(
    val code: Any,
    val id: Int,
    val image: String,
    val menu_id: Int,
    val name: String,
    val products: List<Product>,
    val subcategories: List<Any>
)