package com.example.furnituretask.responses.productbycatId

import com.example.furnituretask.responses.home.Category

data class Data(
    val category: Category,
    val products: List<Product>
)