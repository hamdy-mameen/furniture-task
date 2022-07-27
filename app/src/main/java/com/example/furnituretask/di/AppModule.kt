package com.example.furnituretask.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.example.furnituretask.Constants
import com.example.furnituretask.Constants.Companion.DATABASE_NAME
import com.example.furnituretask.local.FavoriteDatabase
import com.example.furnituretask.remoteData.ApiInterface
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
       return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient,baseUrl:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):ApiInterface = retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context:Context) = context.
    getSharedPreferences(Constants.TOKEN_SHARED_PREF,MODE_PRIVATE)


    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context:Context):FavoriteDatabase =Room.databaseBuilder(context
    ,FavoriteDatabase::class.java,DATABASE_NAME)
        .build()

    @Provides
    @Singleton
    fun provideDao(db:FavoriteDatabase) = db.getFavoriteDao()


}