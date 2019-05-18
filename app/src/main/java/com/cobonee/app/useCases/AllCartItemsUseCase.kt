package com.cobonee.app.useCases

import androidx.lifecycle.LiveData
import com.cobonee.app.R
import com.cobonee.app.entity.CartItem
import com.cobonee.app.storage.local.AppDatabase
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class AllCartItemsUseCase(private val database: AppDatabase) {

    suspend fun getAllCartItems(): DataResource<List<CartItem>>{
        if (Injector.getPreferenceHelper().isLoggedIn) {
            return DataResource.Success(database.cartItemDao().getAllCartItems(Injector.getPreferenceHelper().id))
        } else {
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.error_you_must_login)))
        }
    }
}