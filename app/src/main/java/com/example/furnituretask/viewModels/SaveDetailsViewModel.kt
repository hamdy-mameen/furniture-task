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
class SaveDetailsViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){

    private val _savesDetailsLiveData:MutableLiveData<Resources<SaveDetailsResponse>> =MutableLiveData()
    val savesDetailsLiveData:LiveData<Resources<SaveDetailsResponse>>
        get() = _savesDetailsLiveData



    fun getSaveDetails(id:Int) = viewModelScope.launch {
        try {
            if (isInternetAvailable(getApplication<MyApplication>())){
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
}