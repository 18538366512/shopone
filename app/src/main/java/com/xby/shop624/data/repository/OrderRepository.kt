package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.OrderDao
import com.xby.shop624.data.local.entity.OrderEntity
import com.xby.shop624.data.local.entity.OrderItemEntity
import com.xby.shop624.model.CartItemUi
import com.xby.shop624.model.OrderStatus
import com.xby.shop624.model.OrderWithItems
import com.xby.shop624.model.PaymentMethod
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class OrderRepository(private val orderDao: OrderDao) {

    fun observeOrders(phone: String): Flow<List<OrderWithItems>> =
        orderDao.observeOrdersByPhone(phone)

    suspend fun createOrder(
        userPhone: String,
        shopName: String,
        shopAddress: String,
        items: List<CartItemUi>,
        paymentMethod: PaymentMethod
    ): OrderWithItems {
        val total = items.sumOf { it.subtotal }
        val orderNo = generateOrderNo()
        val deliveryHint = buildNextDayDeliveryHint()
        val orderId = orderDao.insertOrder(
            OrderEntity(
                orderNo = orderNo,
                userPhone = userPhone,
                shopName = shopName,
                shopAddress = shopAddress,
                totalAmount = total,
                paymentMethod = paymentMethod.code,
                status = OrderStatus.PAID.code,
                deliveryHint = deliveryHint
            )
        )
        val orderItems = items.map { item ->
            OrderItemEntity(
                orderId = orderId,
                productId = item.productId,
                productName = item.name,
                productImage = item.imageUrl,
                price = item.price,
                quantity = item.quantity
            )
        }
        orderDao.insertOrderItems(orderItems)
        return orderDao.getOrderWithItems(orderId)
            ?: throw IllegalStateException("订单创建失败")
    }

    suspend fun getOrderCount(phone: String): Int = orderDao.getOrderCount(phone)

    private fun generateOrderNo(): String {
        val time = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(Date())
        val suffix = (1000..9999).random()
        return "BB$time$suffix"
    }

    private fun buildNextDayDeliveryHint(): String {
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val date = SimpleDateFormat("M月d日", Locale.CHINA).format(calendar.time)
        return "预计${date}上午送达门店"
    }
}
