package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE productId = :productId LIMIT 1")
    suspend fun getByProductId(productId: Int): FavoriteEntity?

    @Query("SELECT COUNT(*) FROM favorites WHERE productId = :productId")
    suspend fun isFavorite(productId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getCount(): Int
}