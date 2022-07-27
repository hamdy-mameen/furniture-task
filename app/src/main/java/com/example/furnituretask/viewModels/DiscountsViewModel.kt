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
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.repositories.MainRepository
import com.example.furnituretask.responses.home.CategoryResponse
import com.example.furnituretask.responses.home.DiscountResponse
import com.example.furnituretask.responses.home.DiscountsDetailsResponse
import com.example.furnituretask.responses.home.HomeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class DiscountsViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){
    private val _discountLiveData:MutableLiveData<Resources<DiscountResponse>> =MutableLiveData()
    val discountLiveData:LiveData<Resources<DiscountResponse>>
        get() = _discountLiveData


    init{
        getDiscounts()
    }

    private fun getDiscounts(price:String="DESC") = viewModelScope.launch {
        try {
            if(isInternetAvailable(getApplication<MyApplication>())){
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

}