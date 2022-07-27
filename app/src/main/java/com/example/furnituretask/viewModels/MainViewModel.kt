package com.example.furnituretask.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.*
import com.example.furnituretask.Constants.Companion.AUTH_TOKEN
import com.example.furnituretask.MyApplication
import com.example.furnituretask.local.Favorite
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.repositories.MainRepository
import com.example.furnituretask.responses.LogOutResponse
import com.example.furnituretask.responses.furnitureDetails.FurnitureDetailsResponse
import com.example.furnituretask.responses.home.*
import com.example.furnituretask.responses.nearByFurnitures.NearByFurnituresResponse
import com.example.furnituretask.responses.offerDetails.OfferDetailsResponse
import com.example.furnituretask.responses.password.PasswordResponse
import com.example.furnituretask.responses.productDetails.ProductDetailsResponse
import com.example.furnituretask.responses.productbycatId.ProductByCategoryIdResponse
import com.example.furnituretask.responses.profile.ProfileResponse
import com.example.furnituretask.responses.saveDetails.SaveDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    val app:Application,
    val repository: MainRepository,
    val sharedPreferences: SharedPreferences
): AndroidViewModel(app) {

    private val _homeLiveData:MutableLiveData<Resources<HomeResponse>> =MutableLiveData()
    val homeLiveData:LiveData<Resources<HomeResponse>>
    get() = _homeLiveData
    init{
        getHomeData()
    }
    private val _categoryLiveData:MutableLiveData<Resources<CategoryResponse>> =MutableLiveData()
    val categoryLiveData:LiveData<Resources<CategoryResponse>>
        get() = _categoryLiveData


    private val _discountLiveData:MutableLiveData<Resources<DiscountResponse>> =MutableLiveData()
    val discountLiveData:LiveData<Resources<DiscountResponse>>
        get() = _discountLiveData

    private val _offerLiveData:MutableLiveData<Resources<OfferResponse>> =MutableLiveData()
    val offerLiveData:LiveData<Resources<OfferResponse>>
        get() = _offerLiveData

    private val _savesLiveData:MutableLiveData<Resources<SaveResponse>> =MutableLiveData()
    val savesLiveData:LiveData<Resources<SaveResponse>>
        get() = _savesLiveData

    private val _productByIdLiveData:MutableLiveData<Resources<ProductByCategoryIdResponse>> =MutableLiveData()
    val productByIdLiveData:LiveData<Resources<ProductByCategoryIdResponse>>
        get() = _productByIdLiveData

    private val _productDetailsLiveData:MutableLiveData<Resources<ProductDetailsResponse>> =MutableLiveData()
    val productDetailsLiveData:LiveData<Resources<ProductDetailsResponse>>
        get() = _productDetailsLiveData

    private val _nearByLiveData:MutableLiveData<Resources<NearByFurnituresResponse>> =MutableLiveData()
    val nearByLiveData:LiveData<Resources<NearByFurnituresResponse>>
        get() = _nearByLiveData

    private val _furnitureDetailsLiveData:MutableLiveData<Resources<FurnitureDetailsResponse>> =MutableLiveData()
    val furnitureDetailsLiveData:LiveData<Resources<FurnitureDetailsResponse>>
        get() = _furnitureDetailsLiveData

    private val _offersDetailsLiveData:MutableLiveData<Resources<OfferDetailsResponse>> =MutableLiveData()
    val offersDetailsLiveData:LiveData<Resources<OfferDetailsResponse>>
        get() = _offersDetailsLiveData

    private val _discountDetailsLiveData:MutableLiveData<Resources<DiscountsDetailsResponse>> =MutableLiveData()
    val discountDetailsLiveData:LiveData<Resources<DiscountsDetailsResponse>>
        get() = _discountDetailsLiveData

    private val _savesDetailsLiveData:MutableLiveData<Resources<SaveDetailsResponse>> =MutableLiveData()
    val savesDetailsLiveData:LiveData<Resources<SaveDetailsResponse>>
        get() = _savesDetailsLiveData



    fun getHomeData() = viewModelScope.launch {
       try {
           if (isInternetAvailable()){
               _homeLiveData.postValue(Resources.Loading())
               sharedPreferences.getString(AUTH_TOKEN,"")?.let{
                   val response = repository.getHomeData(it)
                   if (response.isSuccessful){
                       response.body()?.let {homeresponse->
                           _homeLiveData.postValue(Resources.Success(homeresponse))
                       }

                   }else{
                       _homeLiveData.postValue(Resources.Failure(response.message()))
                   }
               }

           }else{
               _homeLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
           }
       }catch (t:Throwable) {
           when (t) {
               is IOException -> _homeLiveData.postValue(Resources.Failure("Network Failure"))
               else -> _homeLiveData.postValue(Resources.Failure("Conversion error"))
           }
       }

    }

    fun getCategory() = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _categoryLiveData.postValue(Resources.Loading())
                val response = repository.getCategories()
                if(response.isSuccessful){
                    response.body()?.let {
                        _categoryLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _categoryLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _categoryLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _categoryLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _categoryLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }

        }


    fun getOffers(price:String="DESC") = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                val response = repository.getOffers(price)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _offerLiveData.postValue(Resources.Success(it))
                    }

                } else {
                    _offerLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _offerLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        }catch (t:Throwable){
            when (t) {
                is IOException -> _offerLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _offerLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }

    }
    fun getDiscounts(price:String="DESC") = viewModelScope.launch {
        try {
            if(isInternetAvailable()){
                val response = repository.getDiscounts(price)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _discountLiveData.postValue(Resources.Success(it))
                    }

                } else {
                    _discountLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _discountLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        }catch (t:Throwable){
            when (t) {
                is IOException -> _discountLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _discountLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }

    }
    fun getSaves(price:String="DESC") = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                val response = repository.getSaves(price)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _savesLiveData.postValue(Resources.Success(it))
                    }

                } else {
                    _savesLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _savesLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _savesLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _savesLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }

    }
    fun getProductByCategoryId(id:Int,price: String="ASC") = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _productByIdLiveData.postValue(Resources.Loading())
                val response = repository.getProductByCategoryId(id, price)
                if(response.isSuccessful){
                    response.body()?.let {
                        _productByIdLiveData.postValue(Resources.Success(it))
                    }

                }else{
                        _productByIdLiveData.postValue(Resources.Failure(response.message()))
                    }

            }else{
                _productByIdLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _productByIdLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _productByIdLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }

    fun getProductDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _productDetailsLiveData.postValue(Resources.Loading())
                sharedPreferences.getString(AUTH_TOKEN,"")?.let{
                    val response = repository.getProductDetails(it,id)
                    if(response.isSuccessful){
                        response.body()?.let {productResponse->
                            _productDetailsLiveData.postValue(Resources.Success(productResponse))
                        }

                    }else{
                        _productDetailsLiveData.postValue(Resources.Failure(response.message()))
                    }
                }


            }else{
                _productDetailsLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _productDetailsLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _productDetailsLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun getNearByFurniture(lat:String,lng:String) = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _nearByLiveData.postValue(Resources.Loading())
                val response = repository.getNearByFurniture(lat, lng)
                if(response.isSuccessful){
                    response.body()?.let {
                        _nearByLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _nearByLiveData.postValue(Resources.Failure(response.message()))
                }

            }else{
                _nearByLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _nearByLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _nearByLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun getFurnitureDetails(id:Int) = viewModelScope.launch {
        try {
            if(isInternetAvailable()){
                _furnitureDetailsLiveData.postValue(Resources.Loading())
                val response = repository.getFurnitureDetails(id)
                if(response.isSuccessful){
                    response.body()?.let {
                        _furnitureDetailsLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _furnitureDetailsLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _furnitureDetailsLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable)
        {
            when (t) {
                is IOException -> _furnitureDetailsLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _furnitureDetailsLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun getOfferDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _offersDetailsLiveData.postValue(Resources.Loading())
                val response = repository.getOfferDetails(id)
                if(response.isSuccessful){
                    response.body()?.let {
                        _offersDetailsLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _offersDetailsLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _offersDetailsLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _offersDetailsLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _offersDetailsLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun getDiscountDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _discountDetailsLiveData.postValue(Resources.Loading())
                val response = repository.getDiscountDetails(id)
                if(response.isSuccessful){
                    response.body()?.let {
                        _discountDetailsLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _discountDetailsLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _discountDetailsLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _discountDetailsLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _discountDetailsLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun getSaveDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable()){
                _savesDetailsLiveData.postValue(Resources.Loading())
                val response = repository.getSavesDetails(id)
                if(response.isSuccessful){
                    response.body()?.let {
                        _savesDetailsLiveData.postValue(Resources.Success(it))
                    }

                }else{
                    _savesDetailsLiveData.postValue(Resources.Failure(response.message()))
                }
            }else{
                _savesDetailsLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }
        }catch (t:Throwable){
            when (t) {
                is IOException -> _savesDetailsLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _savesDetailsLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }

    private val _profileLiveData:MutableLiveData<Resources<ProfileResponse>> =MutableLiveData()
    val profileLiveData:LiveData<Resources<ProfileResponse>>
        get() = _profileLiveData

    fun getProfile() = viewModelScope.launch {
        try {

            if (isInternetAvailable()) {
                sharedPreferences.getString(AUTH_TOKEN, "")?.let {
                    _profileLiveData.postValue(Resources.Loading())
                    val response = repository.getProfile(it)
                    if (response.isSuccessful) {
                        response.body()?.let { profileResponse ->
                            _profileLiveData.postValue(Resources.Success(profileResponse))
                        }

                    } else {
                        _profileLiveData.postValue(Resources.Failure(response.message()))
                    }
                }
            } else {
                _profileLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _profileLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _profileLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }

        private val _passWordLiveData:MutableLiveData<Resources<PasswordResponse>> =MutableLiveData()
        val passWordLiveData:LiveData<Resources<PasswordResponse>>
        get() = _passWordLiveData

        fun updatePassword(old:String,new:String,confirm:String) = viewModelScope.launch {
            try {

                if (isInternetAvailable()) {
                    sharedPreferences.getString(AUTH_TOKEN,"")?.let {
                        _passWordLiveData.postValue(Resources.Loading())
                        val response = repository.updatePassword(it,old, new, confirm)
                        if(response.isSuccessful){
                            response.body()?.let {passResponse->
                                _passWordLiveData.postValue(Resources.Success(passResponse))
                            }

                        }else{
                            _passWordLiveData.postValue(Resources.Failure(response.message()))
                        }
                    }
                } else {
                    _passWordLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> _passWordLiveData.postValue(Resources.Failure("Network Failure"))
                    else -> _passWordLiveData.postValue(Resources.Failure("Conversion error"))
                }
            }

    }
    private val _logOutLiveData:MutableLiveData<Resources<LogOutResponse>> =MutableLiveData()
    val logOutLiveData:LiveData<Resources<LogOutResponse>>
        get() = _logOutLiveData

    fun logOut() = viewModelScope.launch {
        try {
            if (isInternetAvailable()) {
                sharedPreferences.getString(AUTH_TOKEN, "")?.let {
                    _logOutLiveData.postValue(Resources.Loading())
                    val response = repository.logOut(it)
                    if (response.isSuccessful) {
                        response.body()?.let { logResponse ->
                            _logOutLiveData.postValue(Resources.Success(logResponse))
                        }

                    } else {
                        _logOutLiveData.postValue(Resources.Failure(response.message()))
                    }
                }
            } else {
                _logOutLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _logOutLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _logOutLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }

    private fun isInternetAvailable():Boolean{
        val connectivityManager = getApplication<MyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities =connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI)->true
                capabilities.hasTransport(TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(TRANSPORT_ETHERNET)->true
                else->false

            }

        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when (type){
                    TYPE_WIFI ->true
                    TYPE_MOBILE ->true
                    TYPE_ETHERNET ->true
                    else->false
                }
            }
        }
        return false

    }




    //Local
    val favoriteLiveData = repository.getAllFavorites()
    fun insertFavorite(favorite: Favorite) = viewModelScope.launch {
        repository.insertToFavorite(favorite)
    }
}