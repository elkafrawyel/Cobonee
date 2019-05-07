package com.cobonee.app.utily

import com.cobonee.app.MyApp
import com.cobonee.app.repo.DepartmentsRepo
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.useCases.CitiesUseCase
import com.cobonee.app.useCases.DepartmentsUseCase
import com.cobonee.app.useCases.OffersUseCase
import com.cobonee.app.utily.Constants.BASE_URL
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

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

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun getApiService() = RetrofitApiService.create(BASE_URL, getOkHttpClient())

    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())

    //===================================Repo=========================================

    fun getCitiesRepo() = CitiesRepo(getApiService())

    fun getOffersRepo() = OffersRepo(getApiService())

    fun getDepartmentRepo() = DepartmentsRepo(getApiService())

    //=================================== UseCases ====================================

    fun getCitiesUseCase() = CitiesUseCase(getCitiesRepo())

    fun getOffersUseCase() = OffersUseCase(getOffersRepo())

    fun getDepartmentsUseCase() = DepartmentsUseCase(getDepartmentRepo())


}