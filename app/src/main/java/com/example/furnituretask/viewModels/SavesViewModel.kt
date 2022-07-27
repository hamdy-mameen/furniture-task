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
import com.example.furnituretask.responses.home.HomeResponse
import com.example.furnituretask.responses.home.SaveResponse
import com.example.furnituretask.responses.saveDetails.SaveDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class SavesViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){
    private val _savesLiveData:MutableLiveData<Resources<SaveResponse>> =MutableLiveData()
    val savesLiveData:LiveData<Resources<SaveResponse>>
        get() = _savesLiveData


    init{
        getSaves()
    }

    private fun getSaves(price:String="DESC") = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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

}