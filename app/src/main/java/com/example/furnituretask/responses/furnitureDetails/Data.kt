package com.example.furnituretask.responses.furnitureDetails

import com.example.furnituretask.responses.home.Discount
import com.example.furnituretask.responses.home.Offer
import com.example.furnituretask.responses.home.Save
import com.example.furnituretask.responses.productDetails.Rate

data class Data(
    val branches: List<Branche>,
    val discounts: List<Discount>,
    val extras: List<Any>,
    val favourites: List<Any>,
    val furniture: Furniture,
    val menu: List<Menu>,
    val most_wanted_products: List<MostWantedProduct>,
    val offers: List<Offer>,
    val rates: List<Rate>,
    val saves: List<Save>
)