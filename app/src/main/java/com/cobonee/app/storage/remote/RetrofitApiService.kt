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

    @GET("offers")
    fun getAuthOffersAsync(
        @Header("Authorization") token: String,
        @Query("department_id") department_id: String,
        @Query("city_id") city_id: String,
        @Query("page") page: String
    ): Deferred<OffersResponse>

    @POST("auth/login")
    fun getLoginAsync(
        @Body loginBody: LoginBody
    ): Deferred<LoginResponse>

    @POST("auth/login/social")
    fun getLoginFaceAsync(
        @Body loginFaceBody: LoginFaceBody
    ): Deferred<LoginResponse>

    @POST("auth/password/forget")
    fun getForgetAsync(
        @Body forgetBody: ForgetBody
    ): Deferred<ForgetResponse>

    @POST("auth/password/check-code")
    fun getCheckCodeAsync(
        @Body checkCodeBody: CheckCodeBody
    ): Deferred<CheckCodeResponse>

    @POST("auth/password/reset")
    fun getResetAsync(
        @Body resetBody: ResetBody
    ): Deferred<LoginResponse>

    @POST("auth/register")
    fun getRegisterAsync(
        @Body registerBody: RegisterBody
    ): Deferred<LoginResponse>

    @PATCH("profile/update")
    fun updateProfileAsync(
        @Header("Authorization") token: String,
        @Body updateProfileBody: UpdateProfileBody
    ): Deferred<UpdateProfileResponse>

    @GET("settings")
    fun getSettingsAsync(): Deferred<Setting>

    @GET("favourites")
    fun favouritesAsync(
        @Header("Authorization") token: String
    ): Deferred<OffersResponse>

    @POST("favourite/add")
    fun makeFavouritesAsync(
        @Header("Authorization") token: String,
        @Body makeOffersBody: MakeOfferBody
    ): Deferred<MakeFavouritesResponse>

    @POST("unfavourite/{id}")
    fun removeFavouritesAsync(
        @Header("Authorization") token: String,
        @Path("id") offerId: String
    ): Deferred<MakeFavouritesResponse>

    @POST("join/request")
    fun contactUsAsync(
        @Body contactUseBody: ContactUseBody
    ): Deferred<ContactUseResponse>

    @GET("resones")
    fun getReasonsAsync(): Deferred<ReasonsResponse>

    @POST("search/offers")
    fun searchAsync(
        @Query("description") query: String,
        @Query("page") page: String
    ): Deferred<OffersResponse>

    @GET("me/orders")
    fun getAuthOrdersAsync(
        @Header("Authorization") token: String
    ): Deferred<OrdersResponse>

    @POST("check/offers")
    fun getCartItemsAsync(
        @Body cartItemsBody: CartItemsBody
    ): Deferred<CartItemsResponse>

    @POST("orders/create")
    fun createOrderAsync(
        @Header("Authorization") token: String,
        @Body createOrderBody: CreateOrderBody
    ): Deferred<CreateOrderResponse>
}