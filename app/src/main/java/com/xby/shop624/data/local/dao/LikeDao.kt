package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.LikeEntity

@Dao
interface LikeDao {

    @Query("SELECT COUNT(*) FROM likes WHERE productId = :productId")
    suspend fun isLiked(productId: Int): Int

    @Query("SELECT COUNT(*) FROM likes")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(like: LikeEntity)

    @Query("DELETE FROM likes WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)

    @Query("SELECT COUNT(*) FROM likes WHERE productId = :productId")
    suspend fun getLikeCount(productId: Int): Int
}