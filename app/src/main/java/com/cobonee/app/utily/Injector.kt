package com.cobonee.app.utily

import com.cobonee.app.MyApp
import com.cobonee.app.repo.HomeFragmentRepo
import com.cobonee.app.repo.MainRepo
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.useCases.CitiesUseCase
import com.cobonee.app.useCases.OffersUseCase
import com.cobonee.app.utily.Constants.BASE_URL
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Injector {

    fun init() {
        getApiService()
    }

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

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun getApiService() = RetrofitApiService.create(BASE_URL, getOkHttpClient())

    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())

    //===================================Repo=========================================

    fun getMainRepo() = MainRepo(getApiService())

    fun getHomeRepo() = HomeFragmentRepo(getApiService())

    //=================================== UseCases ====================================

    fun getCitiesUseCase() = CitiesUseCase(getMainRepo())

    fun getOffersUseCase() = OffersUseCase(getHomeRepo())


}