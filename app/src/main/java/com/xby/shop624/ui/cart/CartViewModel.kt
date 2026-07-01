package com.xby.shop624.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.model.CartItemUi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository = (application as Shop624App).cartRepository

    val cartItems: LiveData<List<CartItemUi>> =
        cartRepository.observeCartItems().asLiveData()

    val cartCount: LiveData<Int> =
        cartRepository.observeCartCount().asLiveData()

    val selectedTotal: LiveData<String> = cartRepository.observeCartItems()
        .map { items ->
            formatPrice(items.filter { it.selected }.sumOf { it.subtotal })
        }
        .asLiveData()

    val selectedCount: LiveData<Int> = cartRepository.observeCartItems()
        .map { items -> items.count { it.selected } }
        .asLiveData()

    val allSelected: LiveData<Boolean> = cartRepository.observeCartItems()
        .map { items -> items.isNotEmpty() && items.all { it.selected } }
        .asLiveData()

    val isEmpty: LiveData<Boolean> = cartRepository.observeCartItems()
        .map { it.isEmpty() }
        .asLiveData()

    private val _toastMessage = androidx.lifecycle.MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(productId)
            _toastMessage.value = "已加入购物车"
        }
    }

    fun increaseQuantity(cartItemId: Long) {
        viewModelScope.launch { cartRepository.increaseQuantity(cartItemId) }
    }

    fun decreaseQuantity(cartItemId: Long) {
        viewModelScope.launch { cartRepository.decreaseQuantity(cartItemId) }
    }

    fun toggleSelected(cartItemId: Long, selected: Boolean) {
        viewModelScope.launch { cartRepository.toggleSelected(cartItemId, selected) }
    }

    fun setSelectAll(selected: Boolean) {
        viewModelScope.launch { cartRepository.toggleSelectAll(selected) }
    }

    fun deleteItem(cartItemId: Long) {
        viewModelScope.launch { cartRepository.deleteItem(cartItemId) }
    }

    fun canCheckout(): Boolean {
        val items = cartItems.value?.filter { it.selected }.orEmpty()
        if (items.isEmpty()) {
            _toastMessage.value = "请选择要结算的商品"
            return false
        }
        return true
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    private fun formatPrice(value: Double): String {
        return BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }
}
