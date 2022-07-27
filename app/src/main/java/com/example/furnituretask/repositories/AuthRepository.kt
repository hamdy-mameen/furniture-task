package com.example.furnituretask.repositories

import com.example.furnituretask.Registration
import com.example.furnituretask.remoteData.ApiInterface
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val api:ApiInterface
) {
    suspend fun registerUser(registration: Registration) = api.registerUser(registration)
    suspend fun loginUser(userName:String,password:String) = api.loginUser(userName,password)
    suspend fun loginSocial(provider:String,id:String,name:String?,email:String?) = api.loginSocial(provider, id, name, email)
    suspend fun checkSocial(provider:String,id:String) = api.checkSocial(provider,id)
}