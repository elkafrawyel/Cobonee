package com.cobonee.app.useCases

import com.cobonee.app.entity.CartItemsResponse
import com.cobonee.app.entity.Coubone
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.utily.DataResource

class GetCartItemsUseCase(private val offersRepo: OffersRepo) {
    suspend fun getCartItems(offersId:Array<Int>): DataResource<CartItemsResponse> {

        return offersRepo.getCartItems(offersId)
    }
}