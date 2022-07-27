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
import com.example.furnituretask.responses.home.DiscountsDetailsResponse
import com.example.furnituretask.responses.home.HomeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class DiscountDetailsViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){

    private val _discountDetailsLiveData:MutableLiveData<Resources<DiscountsDetailsResponse>> =MutableLiveData()
    val discountDetailsLiveData:LiveData<Resources<DiscountsDetailsResponse>>
        get() = _discountDetailsLiveData


    fun getDiscountDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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
}