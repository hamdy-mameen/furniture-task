package com.example.furnituretask.responses.home

import com.example.furnituretask.responses.home.Discount
import com.example.furnituretask.responses.home.Paginator

data class DiscountResponse(
    val `data`: List<Discount>,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)