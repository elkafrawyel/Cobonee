package com.cobonee.app.storage.remote

import com.cobonee.app.entity.CitiesResponse
import com.cobonee.app.entity.OffersResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("offers")
    fun getOffers(
        @Query("department_id") department_id: String,
        @Query("city_id") city_id: String
    ): Deferred<OffersResponse>

    @GET("cities")
    fun getCities(): Deferred<CitiesResponse>

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