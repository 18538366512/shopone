package com.xby.shop624.model

data class CartItemUi(
    val id: Long,
    val productId: Int,
    val name: String,
    val imageUrl: String,
    val emoji: String?,
    val categoryKey: String,
    val price: Double,
    val quantity: Int,
    val selected: Boolean
) {
    val subtotal: Double get() = price * quantity
}
