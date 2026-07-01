package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val subtitle: String,
    val bgColor: String,
    val imageUrl: String,
    val sortOrder: Int
)
