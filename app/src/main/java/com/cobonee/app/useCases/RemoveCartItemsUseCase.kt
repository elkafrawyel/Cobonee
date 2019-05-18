package com.cobonee.app.useCases

import androidx.lifecycle.LiveData
import com.cobonee.app.R
import com.cobonee.app.entity.CartItem
import com.cobonee.app.storage.local.AppDatabase
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class RemoveCartItemsUseCase(private val database: AppDatabase) {

    suspend fun deleteCartItems(itemId: Int): DataResource<Boolean> {
        if (Injector.getPreferenceHelper().isLoggedIn) {

            database.cartItemDao().deleteCartItem(itemId)
            return DataResource.Success(true)
        } else {
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.error_you_must_login)))
        }
    }
}