package com.xby.shop624.model

import androidx.room.Embedded
import androidx.room.Relation
import com.xby.shop624.data.local.entity.OrderEntity
import com.xby.shop624.data.local.entity.OrderItemEntity

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "id", entityColumn = "orderId")
    val items: List<OrderItemEntity>
)
