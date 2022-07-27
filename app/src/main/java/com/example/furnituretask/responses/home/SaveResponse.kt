package com.example.furnituretask.responses.home

import com.example.furnituretask.responses.home.Paginator
import com.example.furnituretask.responses.home.Save

data class SaveResponse(
    val `data`: List<Save>,
    val message: String,
    val paginator: Paginator,
    val status: Boolean
)