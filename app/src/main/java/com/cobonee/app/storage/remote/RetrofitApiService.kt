package com.cobonee.app.storage.remote

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.DepartmentResponse
import com.cobonee.app.entity.OffersResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("cities")
    fun getCitiesAsync(): Deferred<CityResponse>

    @GET("departments")
    fun getDepartmentsAsync(): Deferred<DepartmentResponse>

    @GET("offers")
    fun getOffersAsync(
        @Query("department_id") department_id: String,
        @Query("city_id") city_id: String,
        @Query("page") page: String
    ): Deferred<OffersResponse>

}