package com.cobonee.app.storage.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cobonee.app.entity.CartItem

@Dao
interface CoboneeDao {

    @Query("SELECT * FROM cartItem WHERE userId LIKE :userId")
    fun getAllCartItems(userId: Int): LiveData<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(vararg item: CartItem)

    @Query("DELETE  FROM cartItem WHERE itemId= :itemId")
    fun deleteCartItem(itemId: Int)

    @Query("SELECT * FROM cartItem WHERE itemId= :itemId")
    fun getCartItem(itemId: Int):CartItem
}