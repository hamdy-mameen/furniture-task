package com.example.furnituretask.responses.productDetails

data class Data(
    val extras: List<Any>,
    val product: Product,
    val rates: List<Rate>
)