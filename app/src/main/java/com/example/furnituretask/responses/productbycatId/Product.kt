package com.example.furnituretask.responses.productbycatId

import com.example.furnituretask.responses.home.Image

data class Product(
    val colors: List<Color>,
    val description: String,
    val furniture: Furniture,
    val icon: String,
    val id: Int,
    val images: List<Image>,
    val is_fav: Boolean,
    val model_type: String,
    val name: String,
    val price: Int,
    val product_id: Int,
    val qty_cart: String,
    val rate: Int,
    val rate_count: Int,
    val sizes: List<Size>,
    val video: Any
)