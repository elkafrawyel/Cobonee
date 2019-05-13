package com.cobonee.app.storage.remote

import com.cobonee.app.entity.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
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
    ): Deferred<LoginResponse>

    @GET("settings")
    fun getSettings(): Deferred<Setting>

    @GET("favourites")
    fun favouritesAsync(
        @Header("Authorization") token: String
    ): Deferred<OffersResponse>

    @POST("favourite/add")
    fun makeFavouritesAsync(
        @Header("Authorization") token: String,
        @Body offerId: Int
    ): Deferred<MakeFavouritesResponse>

    @POST("favourite/remove")
    fun removeFavouritesAsync(
        @Header("Authorization") token: String,
        @Body offerId: Int
    ): Deferred<MakeFavouritesResponse>

    @POST("request")
    fun contactUsAsync(
        @Body contactUseBody: ContactUseBody
    ): Deferred<String>
}