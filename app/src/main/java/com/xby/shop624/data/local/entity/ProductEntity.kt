package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: String,
    val originalPrice: String,
    val tag: String,
    val emoji: String,
    val imageUrl: String,
    val categoryKey: String,
    val subCategoryKey: String,
    val description: String,
    val sales: Int,
    val specs: String = ""
)
