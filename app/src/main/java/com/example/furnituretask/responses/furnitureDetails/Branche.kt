package com.example.furnituretask.responses.furnitureDetails

data class Branche(
    val address: String,
    val branch_id: Int,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val times_of_week: List<TimesOfWeek>
)