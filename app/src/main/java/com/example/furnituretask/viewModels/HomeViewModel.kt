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
import com.example.furnituretask.responses.nearByFurnitures.NearByFurnituresResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository,
    val sharedPreferences: SharedPreferences
): AndroidViewModel(app){
    private val _homeLiveData: MutableLiveData<Resources<HomeResponse>> = MutableLiveData()
    val homeLiveData: LiveData<Resources<HomeResponse>>
        get() = _homeLiveData
    init{
        getHomeData()
    }

    private fun getHomeData() = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
                _homeLiveData.postValue(Resources.Loading())
                sharedPreferences.getString(Constants.AUTH_TOKEN,"")?.let{
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
    private val _nearByLiveData:MutableLiveData<Resources<NearByFurnituresResponse>> =MutableLiveData()
    val nearByLiveData:LiveData<Resources<NearByFurnituresResponse>>
        get() = _nearByLiveData

    fun getNearByFurniture(lat:String,lng:String) = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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
}