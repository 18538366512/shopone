package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.SubCategoryEntity

@Dao
interface SubCategoryDao {

    @Query("SELECT * FROM sub_categories WHERE parentKey = :parentKey ORDER BY sortOrder ASC")
    suspend fun getByParent(parentKey: String): List<SubCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<SubCategoryEntity>)

    @Query("SELECT COUNT(*) FROM sub_categories")
    suspend fun getCount(): Int
}
