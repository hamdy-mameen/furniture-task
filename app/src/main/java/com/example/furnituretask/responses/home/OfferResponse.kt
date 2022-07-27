package com.example.furnituretask.responses.home

data class OfferResponse(
    val `data`: List<Offer>,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)