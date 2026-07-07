package com.xby.shop624.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.LikeEntity
import kotlinx.coroutines.launch

class LikeViewModel(application: Application) : AndroidViewModel(application) {

    private val likeDao = (application as Shop624App).database.likeDao()

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _likeCount = MutableLiveData<Int>()
    val likeCount: LiveData<Int> = _likeCount

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun checkLike(productId: Int) {
        viewModelScope.launch {
            _isLiked.value = likeDao.isLiked(productId) > 0
            _likeCount.value = likeDao.getLikeCount(productId)
        }
    }

    fun toggleLike(productId: Int) {
        viewModelScope.launch {
            val exists = likeDao.isLiked(productId) > 0
            if (exists) {
                likeDao.deleteByProductId(productId)
                _isLiked.value = false
                _likeCount.value = (_likeCount.value ?: 0) - 1
                _toastMessage.value = "已取消点赞"
            } else {
                likeDao.insert(LikeEntity(productId = productId))
                _isLiked.value = true
                _likeCount.value = (_likeCount.value ?: 0) + 1
                _toastMessage.value = "点赞成功"
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}