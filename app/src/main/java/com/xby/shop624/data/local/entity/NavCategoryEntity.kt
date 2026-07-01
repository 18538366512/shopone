package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nav_categories")
data class NavCategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val emoji: String,
    val imageUrl: String,
    val categoryKey: String,
    val sortOrder: Int
)
