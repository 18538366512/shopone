package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.xby.shop624.data.local.entity.OrderEntity
import com.xby.shop624.data.local.entity.OrderItemEntity
import com.xby.shop624.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Transaction
    @Query("SELECT * FROM orders WHERE userPhone = :phone ORDER BY createdAt DESC")
    fun observeOrdersByPhone(phone: String): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems?

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT COUNT(*) FROM orders WHERE userPhone = :phone")
    suspend fun getOrderCount(phone: String): Int
}
