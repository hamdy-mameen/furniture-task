package com.example.furnituretask.responses.saveDetails

import com.example.furnituretask.responses.home.Save

data class SaveDetailsResponse(
    val `data`: Save,
    val message: String,
    val status: Boolean
)