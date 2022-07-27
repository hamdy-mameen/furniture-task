package com.example.furnituretask.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.furnituretask.Constants
import com.example.furnituretask.MyApplication
import com.example.furnituretask.isInternetAvailable
import com.example.furnituretask.local.Favorite
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.repositories.MainRepository
import com.example.furnituretask.responses.home.CategoryResponse
import com.example.furnituretask.responses.home.HomeResponse
import com.example.furnituretask.responses.productDetails.ProductDetailsResponse
import com.example.furnituretask.responses.productbycatId.ProductByCategoryIdResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class ProductByIdViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository,
    val sharedPreferences: SharedPreferences
): AndroidViewModel(app){
    private val _productByIdLiveData:MutableLiveData<Resources<ProductByCategoryIdResponse>> =MutableLiveData()
    val productByIdLiveData:LiveData<Resources<ProductByCategoryIdResponse>>
        get() = _productByIdLiveData


    fun getProductByCategoryId(id:Int,price: String="ASC") = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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

    private val _productDetailsLiveData:MutableLiveData<Resources<ProductDetailsResponse>> =MutableLiveData()
    val productDetailsLiveData:LiveData<Resources<ProductDetailsResponse>>
        get() = _productDetailsLiveData

    fun getProductDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
                _productDetailsLiveData.postValue(Resources.Loading())
                sharedPreferences.getString(Constants.AUTH_TOKEN,"")?.let{
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


    //Local
    val favoriteLiveData = repository.getAllFavorites()
    fun insertFavorite(favorite: Favorite) = viewModelScope.launch {
        repository.insertToFavorite(favorite)
    }
}