package com.cobonee.app.useCases

import androidx.lifecycle.LiveData
import com.cobonee.app.R
import com.cobonee.app.entity.CartItem
import com.cobonee.app.storage.local.AppDatabase
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class AllCartItemsUseCase(private val database: AppDatabase) {

     fun getAllCartItemsLiveData(): LiveData<List<CartItem>> {
         return database.cartItemDao().getAllCartItems(Injector.getPreferenceHelper().id)
    }
}