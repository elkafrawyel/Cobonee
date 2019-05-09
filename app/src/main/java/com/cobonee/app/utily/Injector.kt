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

    //===================================Repo=========================================

    fun getCitiesRepo() = CitiesRepo(getApiService())

    fun getOffersRepo() = OffersRepo(getApiService())

    fun getDepartmentRepo() = DepartmentsRepo(getApiService())

    //=================================== UseCases ====================================

    fun getCitiesUseCase() = CitiesUseCase(getCitiesRepo())

    fun getOffersUseCase() = OffersUseCase(getOffersRepo())

    fun getDepartmentsUseCase() = DepartmentsUseCase(getDepartmentRepo())


}