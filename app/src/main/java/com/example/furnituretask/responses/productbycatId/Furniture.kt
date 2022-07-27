package com.example.furnituretask.responses.productbycatId

data class Furniture(
    val address: String,
    val branch_type_id: Int,
    val branch_type_name: String,
    val description: String,
    val furniture_id: Int,
    val id: Int,
    val is_fav: Boolean,
    val lat: Any,
    val link_apple_store: String,
    val link_google_play: String,
    val lng: Any,
    val logo: String,
    val name: String,
    val `open`: Boolean,
    val qr_image: String,
    val rate: Int,
    val rate_count: Any
)