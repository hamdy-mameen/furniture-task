package com.example.furnituretask.repositories

import com.example.furnituretask.local.Favorite
import com.example.furnituretask.local.FavoriteDao
import com.example.furnituretask.remoteData.ApiInterface
import javax.inject.Inject

class MainRepository @Inject constructor(
   val api:ApiInterface,
   val db:FavoriteDao
) {
    suspend fun getHomeData(token:String) = api.getHomeData(token)
    suspend fun getCategories() = api.getCategories()
    suspend fun getOffers(price:String="DESC") = api.getOffers(price)
    suspend fun getDiscounts(price: String="DESC") = api.getDiscounts(price)
    suspend fun getSaves(price: String="DESC") = api.getSaves(price)
    suspend fun getProductByCategoryId(id:Int,price: String="ASC")= api.getProductByCategoryId(id, price)
    suspend fun getProductDetails(token:String,id:Int)= api.getProductDetails(token,id)
    suspend fun getNearByFurniture(lat:String,lng:String)= api.getNearByFurniture(lat, lng)
    suspend fun getFurnitureDetails(id:Int) = api.getFurnitureDetails(id)
    suspend fun getOfferDetails(id:Int) = api.getOfferDetails(id)
    suspend fun getDiscountDetails(id:Int) = api.getDiscountDetails(id)
    suspend fun getSavesDetails(id:Int) = api.getSavesDetails(id)
    suspend fun getProfile(token: String) = api.getProfile(token)
    suspend fun updatePassword(token: String,old:String,new:String,confirm:String)= api.updatePassword(token,old, new, confirm)
    suspend fun logOut(token: String) = api.logOut(token)

    //local
    suspend fun insertToFavorite(favorite: Favorite) = db.insertFavorite(favorite)
    fun getAllFavorites() = db.getAllFavorite()


}