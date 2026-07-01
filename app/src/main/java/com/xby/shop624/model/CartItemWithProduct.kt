package com.xby.shop624.model

import androidx.room.Embedded
import androidx.room.Relation
import com.xby.shop624.data.local.entity.CartItemEntity
import com.xby.shop624.data.local.entity.ProductEntity

data class CartItemWithProduct(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)
