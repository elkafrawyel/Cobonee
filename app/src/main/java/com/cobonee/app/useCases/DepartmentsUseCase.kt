package com.cobonee.app.useCases

import com.cobonee.app.entity.DepartmentResponse
import com.cobonee.app.repo.DepartmentsRepo
import com.cobonee.app.utily.DataResource

class DepartmentsUseCase(private val departmentsRepo: DepartmentsRepo) {

    suspend fun getDepartments(): DataResource<DepartmentResponse> {

        return departmentsRepo.getDepartments()
    }
}