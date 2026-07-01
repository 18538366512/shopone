package com.xby.shop624

import androidx.multidex.MultiDexApplication
import com.xby.shop624.data.chat.ChatService
import com.xby.shop624.data.local.AppDatabase
import com.xby.shop624.data.repository.CartRepository
import com.xby.shop624.data.repository.CategoryRepository
import com.xby.shop624.data.repository.HomeRepository
import com.xby.shop624.data.repository.OrderRepository
import com.xby.shop624.data.repository.ProductRepository
import com.xby.shop624.util.SessionManager
import com.xby.shop624.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Shop624App : MultiDexApplication() {

    val database by lazy { AppDatabase.getInstance(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val homeRepository by lazy {
        HomeRepository(
            database.bannerDao(),
            database.navCategoryDao(),
            database.productDao()
        )
    }
    val categoryRepository by lazy {
        CategoryRepository(
            database.primaryCategoryDao(),
            database.subCategoryDao(),
            database.productDao()
        )
    }
    val productRepository by lazy {
        ProductRepository(database.productDao(), database.commentDao())
    }
    val cartRepository by lazy { CartRepository(database.cartDao()) }
    val orderRepository by lazy { OrderRepository(database.orderDao()) }
    val sessionManager by lazy { SessionManager(this) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        instance = this
        ChatService.init()
        appScope.launch {
            userRepository.ensureDefaultUser()
            homeRepository.ensureDefaultData()
            categoryRepository.ensureDefaultData()
            productRepository.ensureDefaultComments()
        }
    }

    companion object {
        lateinit var instance: Shop624App
            private set
    }
}
