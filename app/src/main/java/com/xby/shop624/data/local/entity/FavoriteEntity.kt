package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val productId: Int,
    val name: String,
    val price: String,
    val imageUrl: String,
    val emoji: String,
    val tag: String,
    val categoryKey: String,
    val subCategoryKey: String,
    val createdAt: Long = System.currentTimeMillis()
)