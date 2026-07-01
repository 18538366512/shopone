package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNo: String,
    val userPhone: String,
    val shopName: String,
    val shopAddress: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val status: String,
    val deliveryHint: String,
    val createdAt: Long = System.currentTimeMillis()
)
