package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val phone: String,
    val password: String,
    val nickname: String = "便利店主",
    val shopName: String = "幸福社区便利店",
    val shopAddress: String = "幸福路88号",
    val balance: Double = 500.0
)
