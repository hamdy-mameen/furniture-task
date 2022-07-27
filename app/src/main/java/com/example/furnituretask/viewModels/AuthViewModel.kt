package com.example.furnituretask.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.lifecycle.*
import com.example.furnituretask.MyApplication
import com.example.furnituretask.Registration
import com.example.furnituretask.remoteData.Resources
import com.example.furnituretask.repositories.AuthRepository
import com.example.furnituretask.responses.auth.RegistrationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val app: Application,
    val repository: AuthRepository
) : AndroidViewModel(app){
    private var _authLiveData:MutableLiveData<Resources<RegistrationResponse>> = MutableLiveData()
    val authLiveData:LiveData<Resources<RegistrationResponse>>
    get() = _authLiveData


    fun registerUser(registration: Registration) = viewModelScope.launch {

        try {
            if (isInternetAvailable()) {
                val response = repository.registerUser(registration)
                handleResponse(response)

            } else {
                _authLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _authLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _authLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }

    }
    fun loginUser(userName:String,password:String) =viewModelScope.launch {

        try {
            if (isInternetAvailable()) {
                val response = repository.loginUser(userName, password)
                handleResponse(response)

            } else {
                _authLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _authLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _authLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }


    fun loginSocial(provider:String,id:String,name:String?,email:String?) = viewModelScope.launch {
        try {
            if (isInternetAvailable()) {
                val response = repository.loginSocial(provider, id, name, email)
                handleResponse(response)

            } else {
                _authLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _authLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _authLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    fun checkSocial(provider:String,id:String) = viewModelScope.launch {

        try {
            if (isInternetAvailable()) {
                val response = repository.checkSocial(provider,id)
                handleResponse(response)

            } else {
                _authLiveData.postValue(Resources.Failure("لا يوجد اتصال انترنت"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _authLiveData.postValue(Resources.Failure("Network Failure"))
                else -> _authLiveData.postValue(Resources.Failure("Conversion error"))
            }
        }
    }
    private fun handleResponse(response: Response<RegistrationResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                _authLiveData.postValue(Resources.Success(it))
            }
        } else {
            _authLiveData.postValue(Resources.Failure(response.message()))
        }
    }
    private fun isInternetAvailable():Boolean{
        val connectivityManager = getApplication<MyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities =connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else->false

            }

        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when (type){
                    ConnectivityManager.TYPE_WIFI ->true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE ->true
                    ConnectivityManager.TYPE_ETHERNET ->true
                    else->false
                }
            }
        }
        return false

    }
}