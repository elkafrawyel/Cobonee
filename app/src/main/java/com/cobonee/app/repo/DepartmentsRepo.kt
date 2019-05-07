package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.DepartmentResponse
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall

class DepartmentsRepo(private val apiService: RetrofitApiService) {
    //=====================================Departments=========================================================

    suspend fun getDepartments(): DataResource<DepartmentResponse> {
        return safeApiCall(
            call = { departmentsCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun departmentsCall(): DataResource<DepartmentResponse> {
        val response = apiService.getDepartmentsAsync().await()
        return DataResource.Success(response)
    }
    //=======================================================================================================

}