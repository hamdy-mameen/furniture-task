package com.example.furnituretask.responses.furnitureDetails

import com.example.furnituretask.responses.nearByFurnitures.Country
import com.example.furnituretask.responses.nearByFurnitures.Governorate
import com.example.furnituretask.responses.nearByFurnitures.Region

data class Furniture(
    val address: String,
    val branchType: BranchType,
    val country: Country,
    val description: String,
    val email: String,
    val favourited_count: Int,
    val furniture_id: Int,
    val governorate: Governorate,
    val id: Int,
    val is_fav: Boolean,
    val lat: Any,
    val link_apple_store: String,
    val link_google_play: String,
    val lng: Any,
    val logo: String,
    val name: String,
    val `open`: Boolean,
    val phone: String,
    val qr_image: String,
    val rate: Int,
    val rate_count: Int,
    val region: Region,
    val times_of_week: List<TimesOfWeek>
)