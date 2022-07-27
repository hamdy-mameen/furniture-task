package com.example.furnituretask.responses.nearByFurnitures

import com.example.furnituretask.responses.home.Paginator

data class NearByFurnituresResponse(
    val `data`: List<Data>,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)