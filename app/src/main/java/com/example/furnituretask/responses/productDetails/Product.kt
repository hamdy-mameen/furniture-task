package com.example.furnituretask.responses.productDetails

import com.example.furnituretask.responses.home.Image
import com.example.furnituretask.responses.productbycatId.Color
import com.example.furnituretask.responses.productbycatId.Furniture

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
    val ranking: Ranking,
    val rate: Int,
    val rate_count: Int,
    val sizes: List<Any>,
    val video: Any
)