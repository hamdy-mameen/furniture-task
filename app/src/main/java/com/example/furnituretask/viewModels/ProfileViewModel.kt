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
import com.example.furnituretask.responses.LogOutResponse
import com.example.furnituretask.responses.home.CategoryResponse
import com.example.furnituretask.responses.home.HomeResponse
import com.example.furnituretask.responses.password.PasswordResponse
import com.example.furnituretask.responses.profile.ProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    val app: Application,
    val repository: MainRepository,
    val sharedPreferences: SharedPreferences
): AndroidViewModel(app){
    private val _profileLiveData:MutableLiveData<Resources<ProfileResponse>> =MutableLiveData()
    val profileLiveData:LiveData<Resources<ProfileResponse>>
        get() = _profileLiveData

    init{
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch {
        try {

            if (isInternetAvailable(getApplication<MyApplication>())) {
                sharedPreferences.getString(Constants.AUTH_TOKEN, "")?.let {
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

            if (isInternetAvailable(getApplication<MyApplication>())) {
                sharedPreferences.getString(Constants.AUTH_TOKEN,"")?.let {
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
            if (isInternetAvailable(getApplication<MyApplication>())) {
                sharedPreferences.getString(Constants.AUTH_TOKEN, "")?.let {
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

}