package com.xby.shop624.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xby.shop624.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ProductEntity?

    @Query(
        """
        SELECT * FROM products
        WHERE (:categoryKey IS NULL OR categoryKey = :categoryKey)
        ORDER BY id ASC
        """
    )
    suspend fun getByCategory(categoryKey: String?): List<ProductEntity>

    @Query(
        """
        SELECT * FROM products
        WHERE categoryKey = :primaryKey
        AND (:subKey = 'all' OR subCategoryKey = :subKey)
        ORDER BY id ASC
        """
    )
    suspend fun getByPrimaryAndSub(primaryKey: String, subKey: String): List<ProductEntity>

    @Query(
        """
        SELECT * FROM products
        WHERE name LIKE '%' || :keyword || '%'
        AND (:categoryKey IS NULL OR categoryKey = :categoryKey)
        ORDER BY id ASC
        """
    )
    suspend fun search(keyword: String, categoryKey: String?): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int
}
