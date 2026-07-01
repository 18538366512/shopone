package com.xby.shop624.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.UserEntity
import com.xby.shop624.model.CartItemUi
import com.xby.shop624.model.PaymentMethod
import com.xby.shop624.util.PriceUtil
import kotlinx.coroutines.launch
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as Shop624App
    private val cartRepository = app.cartRepository
    private val orderRepository = app.orderRepository
    private val userRepository = app.userRepository
    private val sessionManager = app.sessionManager

    private val _items = MutableLiveData<List<CartItemUi>>()
    val items: LiveData<List<CartItemUi>> = _items

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _totalAmount = MutableLiveData("0")
    val totalAmount: LiveData<String> = _totalAmount

    private val _deliveryHint = MutableLiveData<String>()
    val deliveryHint: LiveData<String> = _deliveryHint

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val _paySuccess = MutableLiveData<Boolean>()
    val paySuccess: LiveData<Boolean> = _paySuccess

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    init {
        loadCheckoutData()
    }

    private fun loadCheckoutData() {
        viewModelScope.launch {
            val phone = sessionManager.getLoggedInPhone()
            if (phone.isNullOrBlank()) {
                _toastMessage.value = "请先登录"
                return@launch
            }
            val selectedItems = cartRepository.getSelectedItems()
            if (selectedItems.isEmpty()) {
                _toastMessage.value = "请选择要结算的商品"
                return@launch
            }
            _items.value = selectedItems
            _totalAmount.value = PriceUtil.format(selectedItems.sumOf { it.subtotal })
            _user.value = userRepository.getUser(phone)
            _deliveryHint.value = buildNextDayDeliveryHint()
        }
    }

    fun pay(paymentMethod: PaymentMethod) {
        val phone = sessionManager.getLoggedInPhone()
        val currentUser = _user.value
        val selectedItems = _items.value.orEmpty()
        if (phone.isNullOrBlank() || currentUser == null) {
            _toastMessage.value = "请先登录"
            return
        }
        if (selectedItems.isEmpty()) {
            _toastMessage.value = "请选择要结算的商品"
            return
        }
        val total = selectedItems.sumOf { it.subtotal }

        viewModelScope.launch {
            _loading.value = true
            if (paymentMethod == PaymentMethod.BALANCE) {
                val result = userRepository.deductBalance(phone, total)
                if (result.isFailure) {
                    _loading.value = false
                    _toastMessage.value = result.exceptionOrNull()?.message ?: "余额支付失败"
                    return@launch
                }
                _user.value = currentUser.copy(balance = result.getOrThrow())
            }
            orderRepository.createOrder(
                userPhone = phone,
                shopName = currentUser.shopName,
                shopAddress = currentUser.shopAddress,
                items = selectedItems,
                paymentMethod = paymentMethod
            )
            cartRepository.clearSelectedItems()
            _loading.value = false
            _paySuccess.value = true
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun clearPaySuccess() {
        _paySuccess.value = false
    }

    private fun buildNextDayDeliveryHint(): String {
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val date = SimpleDateFormat("M月d日", Locale.CHINA).format(calendar.time)
        return "订单成功后，预计${date}上午配送到店"
    }
}
