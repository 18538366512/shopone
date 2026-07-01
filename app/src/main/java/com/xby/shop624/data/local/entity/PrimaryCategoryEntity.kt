package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "primary_categories")
data class PrimaryCategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val categoryKey: String,
    val imageUrl: String,
    val sortOrder: Int
)
