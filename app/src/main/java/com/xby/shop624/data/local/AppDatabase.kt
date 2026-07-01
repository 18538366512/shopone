package com.xby.shop624.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xby.shop624.data.local.dao.BannerDao
import com.xby.shop624.data.local.dao.CartDao
import com.xby.shop624.data.local.dao.CommentDao
import com.xby.shop624.data.local.dao.NavCategoryDao
import com.xby.shop624.data.local.dao.OrderDao
import com.xby.shop624.data.local.dao.PrimaryCategoryDao
import com.xby.shop624.data.local.dao.ProductDao
import com.xby.shop624.data.local.dao.SubCategoryDao
import com.xby.shop624.data.local.dao.UserDao
import com.xby.shop624.data.local.entity.BannerEntity
import com.xby.shop624.data.local.entity.CartItemEntity
import com.xby.shop624.data.local.entity.CommentEntity
import com.xby.shop624.data.local.entity.NavCategoryEntity
import com.xby.shop624.data.local.entity.OrderEntity
import com.xby.shop624.data.local.entity.OrderItemEntity
import com.xby.shop624.data.local.entity.PrimaryCategoryEntity
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.data.local.entity.SubCategoryEntity
import com.xby.shop624.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        BannerEntity::class,
        NavCategoryEntity::class,
        ProductEntity::class,
        PrimaryCategoryEntity::class,
        SubCategoryEntity::class,
        CommentEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 22,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bannerDao(): BannerDao
    abstract fun navCategoryDao(): NavCategoryDao
    abstract fun productDao(): ProductDao
    abstract fun primaryCategoryDao(): PrimaryCategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun commentDao(): CommentDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop624.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
