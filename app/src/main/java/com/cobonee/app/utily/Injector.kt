package com.cobonee.app.utily

import com.cobonee.app.MyApp
import com.cobonee.app.repo.*
import com.cobonee.app.repo.DepartmentsRepo
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.storage.local.AppDatabase
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.useCases.*
import com.cobonee.app.useCases.CitiesUseCase
import com.cobonee.app.useCases.DepartmentsUseCase
import com.cobonee.app.useCases.OffersUseCase
import com.cobonee.app.utily.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Injector {

    private val coroutinesDispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    fun getApplicationContext() = MyApp.instance
    fun getCoroutinesDispatcherProvider() = coroutinesDispatcherProvider
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getApiServiceHeader(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Accept", "application/json")

            if (chain.request().header("Accept-Language") == null) {
                request.addHeader(
                    "Accept-Language",
                    chain.request().header("Accept-Language") ?: getPreferenceHelper().language
                )
            }

            chain.proceed(request.build())
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getApiServiceHeader())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }


    private fun create(baseUrl: String, client: OkHttpClient): RetrofitApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(client)
            .build()

        return retrofit.create(RetrofitApiService::class.java)
    }

    private fun getApiService() = create(BASE_URL, getOkHttpClient())

    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())

    fun getAppDatabase() = AppDatabase.invoke(Injector.getApplicationContext())

    //===================================Repo=========================================

    private fun getCitiesRepo() = CitiesRepo(getApiService())

    private fun getOffersRepo() = OffersRepo(getApiService(), getPreferenceHelper())

    private fun getDepartmentRepo() = DepartmentsRepo(getApiService())

    private fun getLoginRepo() = LoginRepo(getApiService())

    private fun getRegisterRepo() = RegisterRepo(getApiService())


    private fun getUserRepo() = UserRepo(getPreferenceHelper(), getApiService())

    private fun getFavouritesRepo() = FavouritesRepo(getApiService(), getPreferenceHelper())

    private fun getSettingsRepo() = SettingsRepo(getApiService())

    //=================================== UseCases ====================================

    fun getCitiesUseCase() = CitiesUseCase(getCitiesRepo())

    fun getOffersUseCase() = OffersUseCase(getOffersRepo())

    fun getDepartmentsUseCase() = DepartmentsUseCase(getDepartmentRepo())

    fun getLoginUseCase() = LoginUseCase(getLoginRepo())

    fun getRegisterUseCase() = RegisterUseCase(getRegisterRepo())

    fun getSaveUserUseCase() = SaveUserUseCase(getUserRepo())

    fun getUpdateProfileUseCase() = UpdateProfileUseCase(getUserRepo())

    fun getUserUseCase() = GetUserUseCase(getUserRepo())

    fun getFavouritesUseCase() = FavouritesUseCase(getFavouritesRepo())

    fun getAddOfferToFavouritesUseCase() = MakeOfferFavouritesUseCase(getFavouritesRepo())

    fun getRemoveOfferToFavouritesUseCase() = RemoveFavouritesUseCase(getFavouritesRepo())

    fun getSettingsUseCase() = SettingsUseCase(getSettingsRepo())

    fun getContactUsUserUseCase() = ContactUsUseCase(getUserRepo())

    fun getReasonsUseCase() = ReasonsUseCase(getSettingsRepo())

    fun getSearchUseCase() = SearchUseCase(offersRepo = getOffersRepo())

    fun getAllCartItemsUseCase() = AllCartItemsUseCase(getAppDatabase())

    fun getAddCartItemsUseCase() = AddCartItemsUseCase(getAppDatabase())

    fun getRemoveCartItemsUseCase() = RemoveCartItemsUseCase(getAppDatabase())

}