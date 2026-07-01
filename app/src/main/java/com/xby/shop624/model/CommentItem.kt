package com.xby.shop624.model

import com.xby.shop624.data.local.entity.CommentEntity

data class CommentItem(
    val comment: CommentEntity,
    val replies: List<CommentEntity> = emptyList()
)
