package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey
    val productId: Int,
    val createdAt: Long = System.currentTimeMillis()
)