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
import com.example.furnituretask.responses.home.HomeResponse
import com.example.furnituretask.responses.home.OfferResponse
import com.example.furnituretask.responses.offerDetails.OfferDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class OffersDetailsViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){



    private val _offersDetailsLiveData:MutableLiveData<Resources<OfferDetailsResponse>> =MutableLiveData()
    val offersDetailsLiveData:LiveData<Resources<OfferDetailsResponse>>
        get() = _offersDetailsLiveData

    fun getOfferDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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
}