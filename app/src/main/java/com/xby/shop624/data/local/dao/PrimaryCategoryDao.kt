package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.PrimaryCategoryEntity

@Dao
interface PrimaryCategoryDao {

    @Query("SELECT * FROM primary_categories ORDER BY sortOrder ASC")
    suspend fun getAll(): List<PrimaryCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<PrimaryCategoryEntity>)

    @Query("SELECT COUNT(*) FROM primary_categories")
    suspend fun getCount(): Int
}
