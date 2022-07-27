package com.example.furnituretask.responses.home

import com.example.furnituretask.responses.home.Discount

data class DiscountsDetailsResponse(
    val `data`: Discount,
    val message: String,
    val status: Boolean
)