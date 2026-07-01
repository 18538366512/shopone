package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.xby.shop624.data.local.entity.CartItemEntity
import com.xby.shop624.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Transaction
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun observeCartWithProducts(): Flow<List<CartItemWithProduct>>

    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CartItemEntity?

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getByProductId(productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity): Long

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("UPDATE cart_items SET selected = :selected WHERE id = :id")
    suspend fun updateSelected(id: Long, selected: Boolean)

    @Query("UPDATE cart_items SET selected = :selected")
    suspend fun updateAllSelected(selected: Boolean)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeTotalQuantity(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM cart_items WHERE selected = 1 ORDER BY id DESC")
    suspend fun getSelectedWithProducts(): List<CartItemWithProduct>

    @Query("DELETE FROM cart_items WHERE selected = 1")
    suspend fun deleteSelected()
}
