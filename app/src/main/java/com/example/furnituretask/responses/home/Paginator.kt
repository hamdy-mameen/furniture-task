package com.example.furnituretask.responses.home

data class Paginator(
    val count: Int,
    val currentPage: Int,
    val hasMorePages: Boolean,
    val lastPage: Int,
    val nextPageUrl: Any,
    val previousPageUrl: Any,
    val total: Int
)