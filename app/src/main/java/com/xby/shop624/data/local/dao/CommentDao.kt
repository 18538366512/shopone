package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xby.shop624.data.local.entity.CommentEntity

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE productId = :productId ORDER BY createTime ASC")
    suspend fun getByProduct(productId: Int): List<CommentEntity>

    @Insert
    suspend fun insert(comment: CommentEntity): Long

    @Query("SELECT COUNT(*) FROM comments WHERE productId = :productId")
    suspend fun getCountByProduct(productId: Int): Int

    @Query("SELECT COUNT(*) FROM comments")
    suspend fun getCount(): Int
}
