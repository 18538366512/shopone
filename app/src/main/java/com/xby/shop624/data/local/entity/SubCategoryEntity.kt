package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_categories")
data class SubCategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val parentKey: String,
    val subKey: String,
    val sortOrder: Int
)
