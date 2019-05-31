package com.cobonee.app.useCases

import com.cobonee.app.R
import com.cobonee.app.entity.CartItem
import com.cobonee.app.storage.local.AppDatabase
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class AddCartItemsUseCase(private val database: AppDatabase) {

    fun addCartItems(itemId: Int, itemQuantity: Int): DataResource<Boolean> {
        if (Injector.getPreferenceHelper().isLoggedIn) {
//            if (database.cartItemDao().getCartItem(itemId) == null) {
                val cartItem = CartItem(itemId, Injector.getPreferenceHelper().id, itemQuantity)
                database.cartItemDao().insertCartItem(cartItem)
//            }
            return DataResource.Success(true)
        } else {
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.error_you_must_login)))
        }
    }
}