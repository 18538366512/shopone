package com.xby.shop624.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = (application as Shop624App).userRepository

    private val _loginResult = MutableLiveData<Result<UserEntity>>()
    val loginResult: LiveData<Result<UserEntity>> = _loginResult

    private val _registerResult = MutableLiveData<Result<UserEntity>>()
    val registerResult: LiveData<Result<UserEntity>> = _registerResult

    fun login(phone: String, password: String) {
        if (phone.isBlank() || password.isBlank()) {
            _loginResult.value = Result.failure(IllegalArgumentException("请输入手机号和密码"))
            return
        }
        viewModelScope.launch {
            val user = userRepository.login(phone.trim(), password)
            if (user != null) {
                _loginResult.postValue(Result.success(user))
            } else {
                _loginResult.postValue(Result.failure(IllegalArgumentException("手机号或密码错误")))
            }
        }
    }

    fun register(phone: String, password: String) {
        viewModelScope.launch {
            _registerResult.postValue(userRepository.register(phone.trim(), password))
        }
    }
}
