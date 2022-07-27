package com.example.furnituretask.responses.productbycatId

import com.example.furnituretask.responses.home.Paginator

data class ProductByCategoryIdResponse(
    val `data`: Data,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)