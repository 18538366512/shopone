package com.xby.shop624.ui.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.xby.shop624.Shop624App

class OrderListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as Shop624App
    private val phone = app.sessionManager.getLoggedInPhone().orEmpty()

    val orders = app.orderRepository.observeOrders(phone).asLiveData()
}
