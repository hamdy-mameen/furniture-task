package com.example.furnituretask.responses.discountDetails

import com.example.furnituretask.responses.home.Discount

data class DiscountDetailsResponse(
    val `data`: Discount,
    val message: String,
    val status: Boolean
)