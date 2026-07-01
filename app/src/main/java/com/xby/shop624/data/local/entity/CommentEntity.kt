package com.xby.shop624.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Int,
    val userName: String,
    val content: String,
    val createTime: Long,
    val parentId: Long? = null
)
