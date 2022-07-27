package com.example.furnituretask.remoteData

import com.example.furnituretask.Registration
import com.example.furnituretask.responses.LogOutResponse
import com.example.furnituretask.responses.auth.RegistrationResponse
import com.example.furnituretask.responses.furnitureDetails.FurnitureDetailsResponse
import com.example.furnituretask.responses.home.*
import com.example.furnituretask.responses.nearByFurnitures.NearByFurnituresResponse
import com.example.furnituretask.responses.offerDetails.OfferDetailsResponse
import com.example.furnituretask.responses.password.PasswordResponse
import com.example.furnituretask.responses.productDetails.ProductDetailsResponse
import com.example.furnituretask.responses.productbycatId.ProductByCategoryIdResponse
import com.example.furnituretask.responses.profile.ProfileResponse
import com.example.furnituretask.responses.saveDetails.SaveDetailsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
@POST("register")
suspend fun registerUser(
    @Body registration: Registration
):Response<RegistrationResponse>


@FormUrlEncoded
@POST("login")
suspend fun loginUser(
    @Field("username") userName:String,
    @Field("password") password:String,
):Response<RegistrationResponse>

@FormUrlEncoded
@POST("login/social")
suspend fun loginSocial(
    @Field("provider_type") provider:String,
    @Field("provider_id") id:String,
    @Field("name") name:String?,
    @Field("email") email:String?,
):Response<RegistrationResponse>

    @FormUrlEncoded
    @POST("check/social")
    suspend fun checkSocial(
        @Field("provider_type") provider:String,
        @Field("provider_id") id:String
    ):Response<RegistrationResponse>

@GET("home")
    suspend fun getHomeData(@Header ("Authorization") token:String):Response<HomeResponse>

@GET("home/offers")
    suspend fun getOffers(
  @Query ("price") price:String
    ):Response<OfferResponse>

@GET("home/categories")
suspend fun getCategories():Response<CategoryResponse>

@GET("home/saves")
suspend fun getSaves(
    @Query ("price") price: String
):Response<SaveResponse>
    @GET("home/discounts")
    suspend fun getDiscounts(
        @Query ("price") price: String
    ):Response<DiscountResponse>

    @GET("home/product-by-category/{id}")
 suspend fun getProductByCategoryId(
@Path("id") id: Int,
@Query("price") price: String
 ):Response<ProductByCategoryIdResponse>

 @GET("product/{id}")
 suspend fun getProductDetails(
     @Header ("Authorization") token:String,
     @Path("id") id: Int
 ):Response<ProductDetailsResponse>

 @POST("home/furniture-nearby")
 suspend fun getNearByFurniture(
     @Query("lat") lat:String,
     @Query("lng") lng:String
 ):Response<NearByFurnituresResponse>

 @GET("furniture/{id}")
 suspend fun getFurnitureDetails(
     @Path("id") id:Int
 ):Response<FurnitureDetailsResponse>

    @GET("home/saves/{id}")
    suspend fun getSavesDetails(
        @Path("id") id:Int
    ):Response<SaveDetailsResponse>
    @GET("home/offers/{id}")
    suspend fun getOfferDetails(
        @Path("id") id:Int
    ):Response<OfferDetailsResponse>
    @GET("home/discounts/{id}")
    suspend fun getDiscountDetails(
        @Path("id") id:Int
    ):Response<DiscountsDetailsResponse>

    @GET("profile")
    suspend fun getProfile(
        @Header ("Authorization") token:String
    ):Response<ProfileResponse>

    @POST("update-password")
    suspend fun updatePassword(
        @Header ("Authorization") token:String,
        @Query ("old_password") old:String,
        @Query ("password") new:String,
        @Query ("password_confirmation") confirm:String,
    ):Response<PasswordResponse>

    @GET("logout")
    suspend fun logOut(
        @Header ("Authorization") token:String
    ):Response<LogOutResponse>
}