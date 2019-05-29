package com.cobonee.app.useCases

import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.entity.OrdersResponse
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.repo.OrdersRepo
import com.cobonee.app.utily.DataResource

class OrdersUseCase(private val orderssRepo: OrdersRepo) {

    suspend fun getOrders(): DataResource<OrdersResponse> {
        return orderssRepo.getOrders()
    }
}