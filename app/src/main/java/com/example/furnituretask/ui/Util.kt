package com.example.furnituretask

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import android.view.View
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.Contexts.getApplication


fun View.enable(enable:Boolean){
    isEnabled = enable
}
fun View.showSnack(message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}
var clickEvent:((Int)->Unit)?=null
fun setListener(event:(Int)->Unit){
    clickEvent = event
}
fun isInternetAvailable(app:Application):Boolean{
    val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
