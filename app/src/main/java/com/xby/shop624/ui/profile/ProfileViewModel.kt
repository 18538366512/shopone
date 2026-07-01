package com.xby.shop624.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as Shop624App

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _orderCount = MutableLiveData(0)
    val orderCount: LiveData<Int> = _orderCount

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun loadProfile() {
        viewModelScope.launch {
            val phone = app.sessionManager.getLoggedInPhone() ?: return@launch
            _user.value = app.userRepository.getUser(phone)
            _orderCount.value = app.orderRepository.getOrderCount(phone)
        }
    }

    fun updateNickname(newName: String) {
        viewModelScope.launch {
            val phone = app.sessionManager.getLoggedInPhone() ?: return@launch
            app.userRepository.updateNickname(phone, newName)
                .onSuccess { user ->
                    _user.value = user
                    _toastMessage.value = "姓名修改成功"
                }
                .onFailure { e ->
                    _toastMessage.value = e.message ?: "修改失败"
                }
        }
    }

    fun updateShopName(newName: String) {
        viewModelScope.launch {
            val phone = app.sessionManager.getLoggedInPhone() ?: return@launch
            app.userRepository.updateShopName(phone, newName)
                .onSuccess { user ->
                    _user.value = user
                    _toastMessage.value = "店铺名修改成功"
                }
                .onFailure { e ->
                    _toastMessage.value = e.message ?: "修改失败"
                }
        }
    }

    fun updateShopAddress(newAddress: String) {
        viewModelScope.launch {
            val phone = app.sessionManager.getLoggedInPhone() ?: return@launch
            app.userRepository.updateShopAddress(phone, newAddress)
                .onSuccess { user ->
                    _user.value = user
                    _toastMessage.value = "地址修改成功"
                }
                .onFailure { e ->
                    _toastMessage.value = e.message ?: "修改失败"
                }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
