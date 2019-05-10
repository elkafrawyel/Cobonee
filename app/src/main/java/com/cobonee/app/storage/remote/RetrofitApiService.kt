package com.cobonee.app.storage.remote

import com.cobonee.app.entity.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

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

    @POST("auth/login")
    fun getLoginAsync(
        @Body loginBody: LoginBody
    ): Deferred<LoginResponse>

    @POST("auth/register")
    fun getRegisterAsync(
        @Body registerBody: RegisterBody
    ): Deferred<LoginResponse>

    @PATCH("profile/update")
    fun updateProfileAsync(
        @Header("Authorization") token: String,
        @Body updateProfileBody: UpdateProfileBody
    ):Deferred<LoginResponse>

}