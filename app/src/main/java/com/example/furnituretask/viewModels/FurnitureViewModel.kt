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
import com.example.furnituretask.responses.furnitureDetails.FurnitureDetailsResponse
import com.example.furnituretask.responses.home.CategoryResponse
import com.example.furnituretask.responses.home.HomeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class FurnitureViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository
): AndroidViewModel(app){

    private val _furnitureDetailsLiveData:MutableLiveData<Resources<FurnitureDetailsResponse>> =MutableLiveData()
    val furnitureDetailsLiveData:LiveData<Resources<FurnitureDetailsResponse>>
        get() = _furnitureDetailsLiveData

     var setIdViewModel:MutableLiveData<Int> = MutableLiveData()
    fun getFurnitureDetails(id:Int) = viewModelScope.launch {
        try {
            if(isInternetAvailable(getApplication<MyApplication>())){
                _furnitureDetailsLiveData.postValue(Resources.Loading())
                val response = repository.getFurnitureDetails(id)
                if(response.isSuccessful){
                    response.body()?.let {
                        _furnitureDetailsLiveData.postValue(Resources.Success(it))
                        setIdViewModel.postValue(id)
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
}