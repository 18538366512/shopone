package com.xby.shop624.data.repository

import com.xby.shop624.data.local.dao.CommentDao
import com.xby.shop624.data.local.dao.PrimaryCategoryDao
import com.xby.shop624.data.local.dao.ProductDao
import com.xby.shop624.data.local.dao.SubCategoryDao
import com.xby.shop624.data.local.entity.CommentEntity
import com.xby.shop624.data.local.entity.PrimaryCategoryEntity
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.data.local.entity.SubCategoryEntity

class CategoryRepository(
    private val primaryCategoryDao: PrimaryCategoryDao,
    private val subCategoryDao: SubCategoryDao,
    private val productDao: ProductDao
) {

    suspend fun getPrimaryCategories(): List<PrimaryCategoryEntity> =
        primaryCategoryDao.getAll()

    suspend fun getSubCategories(parentKey: String): List<SubCategoryEntity> =
        subCategoryDao.getByParent(parentKey)

    suspend fun getProducts(primaryKey: String, subKey: String): List<ProductEntity> =
        productDao.getByPrimaryAndSub(primaryKey, subKey)

    suspend fun ensureDefaultData() {
        if (primaryCategoryDao.getCount() == 0) {
            primaryCategoryDao.insertAll(defaultPrimaryCategories())
        }
        if (subCategoryDao.getCount() == 0) {
            subCategoryDao.insertAll(defaultSubCategories())
        }
    }

    companion object {
        private fun defaultPrimaryCategories() = listOf(
            PrimaryCategoryEntity(1, "酒水饮料", "drink", "drawable://icon_drink", 1),
            PrimaryCategoryEntity(2, "休闲零食", "snack", "drawable://icon_snack", 2),
            PrimaryCategoryEntity(3, "粮油调味", "grain", "drawable://icon_grain", 3),
            PrimaryCategoryEntity(4, "日化用品", "daily", "drawable://icon_daily", 4),
            PrimaryCategoryEntity(5, "牛奶乳品", "milk", "drawable://icon_milk", 5),
            PrimaryCategoryEntity(6, "方便食品", "instant", "drawable://icon_instant", 6)
        )

        private fun defaultSubCategories() = listOf(
            // 酒水饮料
            SubCategoryEntity(101, "全部", "drink", "all", 1),
            SubCategoryEntity(102, "矿泉水", "drink", "water", 2),
            SubCategoryEntity(103, "啤酒", "drink", "beer", 3),
            SubCategoryEntity(104, "茶饮料", "drink", "tea", 4),
            SubCategoryEntity(105, "碳酸饮料", "drink", "cola", 5),

            // 休闲零食
            SubCategoryEntity(201, "全部", "snack", "all", 1),
            SubCategoryEntity(202, "薯片", "snack", "chips", 2),
            SubCategoryEntity(203, "饼干", "snack", "biscuit", 3),
            SubCategoryEntity(204, "坚果", "snack", "nuts", 4),
            SubCategoryEntity(205, "糖果", "snack", "candy", 5),

            // 粮油调味
            SubCategoryEntity(301, "全部", "grain", "all", 1),
            SubCategoryEntity(302, "大米", "grain", "rice", 2),
            SubCategoryEntity(303, "食用油", "grain", "oil", 3),
            SubCategoryEntity(304, "酱油", "grain", "soy", 4),
            SubCategoryEntity(305, "调味品", "grain", "seasoning", 5),

            // 日化用品
            SubCategoryEntity(401, "全部", "daily", "all", 1),
            SubCategoryEntity(402, "纸巾", "daily", "tissue", 2),
            SubCategoryEntity(403, "清洁", "daily", "clean", 3),
            SubCategoryEntity(404, "洗衣", "daily", "laundry", 4),
            SubCategoryEntity(405, "口腔", "daily", "tooth", 5),

            // 牛奶乳品
            SubCategoryEntity(501, "全部", "milk", "all", 1),
            SubCategoryEntity(502, "纯牛奶", "milk", "milk", 2),
            SubCategoryEntity(503, "酸奶", "milk", "yogurt", 3),

            // 方便食品
            SubCategoryEntity(601, "全部", "instant", "all", 1),
            SubCategoryEntity(602, "方便面", "instant", "noodle", 2),
            SubCategoryEntity(603, "冲饮", "instant", "milk", 3)
        )
    }
}
