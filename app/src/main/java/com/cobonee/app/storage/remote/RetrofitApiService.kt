package com.cobonee.app.storage.remote

import com.cobonee.app.utily.ApiResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApiService {

//    @POST("register")
//    fun register(@Body registerRequest: RegisterRequest): Deferred<ApiResponse<User>>
//
//    @POST("doLogin")
//    fun login(@Body loginRequest: LoginRequest): Deferred<ApiResponse<User>>

    companion object {
        fun create(baseUrl: String, client: OkHttpClient): RetrofitApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(client)
                .build()

            return retrofit.create(RetrofitApiService::class.java)
        }
    }
}