package com.xby.shop624.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.FavoriteEntity
import com.xby.shop624.data.local.entity.ProductEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDao = (application as Shop624App).database.favoriteDao()

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun checkFavorite(productId: Int) {
        viewModelScope.launch {
            _isFavorite.value = favoriteDao.isFavorite(productId) > 0
        }
    }

    fun toggleFavorite(product: ProductEntity) {
        viewModelScope.launch {
            val exists = favoriteDao.isFavorite(product.id) > 0
            if (exists) {
                favoriteDao.deleteByProductId(product.id)
                _isFavorite.value = false
                _toastMessage.value = "已取消收藏"
            } else {
                val favorite = FavoriteEntity(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    emoji = product.emoji,
                    tag = product.tag,
                    categoryKey = product.categoryKey,
                    subCategoryKey = product.subCategoryKey
                )
                favoriteDao.insert(favorite)
                _isFavorite.value = true
                _toastMessage.value = "收藏成功"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}