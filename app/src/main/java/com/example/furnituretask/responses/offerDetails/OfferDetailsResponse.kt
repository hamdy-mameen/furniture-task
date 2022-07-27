package com.example.furnituretask.responses.offerDetails

import com.example.furnituretask.responses.home.Offer

data class OfferDetailsResponse(
    val `data`: Offer,
    val message: String,
    val status: Boolean
)