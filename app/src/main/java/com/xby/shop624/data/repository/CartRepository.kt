package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.CartDao
import com.xby.shop624.data.local.entity.CartItemEntity
import com.xby.shop624.model.CartItemUi
import com.xby.shop624.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val cartDao: CartDao) {

    fun observeCartItems(): Flow<List<CartItemUi>> =
        cartDao.observeCartWithProducts().map { list -> list.map { it.toUi() } }

    fun observeCartCount(): Flow<Int> = cartDao.observeTotalQuantity()

    suspend fun addToCart(productId: Int) {
        val existing = cartDao.getByProductId(productId)
        if (existing != null) {
            cartDao.updateQuantity(existing.id, existing.quantity + 1)
        } else {
            cartDao.insert(
                CartItemEntity(productId = productId, quantity = 1, selected = true)
            )
        }
    }

    suspend fun increaseQuantity(cartItemId: Long) {
        val item = cartDao.getById(cartItemId) ?: return
        cartDao.updateQuantity(cartItemId, item.quantity + 1)
    }

    suspend fun decreaseQuantity(cartItemId: Long) {
        val item = cartDao.getById(cartItemId) ?: return
        if (item.quantity <= 1) {
            cartDao.deleteById(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, item.quantity - 1)
        }
    }

    suspend fun toggleSelected(cartItemId: Long, selected: Boolean) {
        cartDao.updateSelected(cartItemId, selected)
    }

    suspend fun toggleSelectAll(selected: Boolean) {
        cartDao.updateAllSelected(selected)
    }

    suspend fun deleteItem(cartItemId: Long) {
        cartDao.deleteById(cartItemId)
    }

    suspend fun getSelectedItems(): List<CartItemUi> =
        cartDao.getSelectedWithProducts().map { it.toUi() }

    suspend fun clearSelectedItems() {
        cartDao.deleteSelected()
    }

    private fun CartItemWithProduct.toUi(): CartItemUi {
        val price = product.price.toDoubleOrNull() ?: 0.0
        return CartItemUi(
            id = cartItem.id,
            productId = product.id,
            name = product.name,
            imageUrl = product.imageUrl,
            emoji = product.emoji,
            categoryKey = product.categoryKey,
            price = price,
            quantity = cartItem.quantity,
            selected = cartItem.selected
        )
    }
}
