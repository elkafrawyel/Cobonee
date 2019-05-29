package com.cobonee.app.useCases

import com.cobonee.app.entity.CreateOrderResponse
import com.cobonee.app.repo.OrdersRepo
import com.cobonee.app.utily.DataResource

class CreateOrderUseCase(private var ordersRepo: OrdersRepo) {
    suspend fun createOrder(idsList: Array<Int>, quantitiesList: Array<Int>): DataResource<CreateOrderResponse> {

        return ordersRepo.createOrder(idsList, quantitiesList)
    }
}