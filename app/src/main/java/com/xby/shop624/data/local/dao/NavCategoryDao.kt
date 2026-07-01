package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.NavCategoryEntity

@Dao
interface NavCategoryDao {

    @Query("SELECT * FROM nav_categories ORDER BY sortOrder ASC")
    suspend fun getAll(): List<NavCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<NavCategoryEntity>)

    @Query("SELECT COUNT(*) FROM nav_categories")
    suspend fun getCount(): Int
}
