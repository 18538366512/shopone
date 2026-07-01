package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.BannerEntity

@Dao
interface BannerDao {

    @Query("SELECT * FROM banners ORDER BY sortOrder ASC")
    suspend fun getAll(): List<BannerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(banners: List<BannerEntity>)

    @Query("SELECT COUNT(*) FROM banners")
    suspend fun getCount(): Int
}
