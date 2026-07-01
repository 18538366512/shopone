package com.xby.shop624.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.model.CommentItem
import kotlinx.coroutines.launch

class ProductDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as Shop624App).productRepository

    private val _product = MutableLiveData<ProductEntity?>()
    val product: LiveData<ProductEntity?> = _product

    private val _comments = MutableLiveData<List<CommentItem>>()
    val comments: LiveData<List<CommentItem>> = _comments

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private var productId: Int = 0
    private val currentUser = "便利用户"

    fun load(productId: Int) {
        this.productId = productId
        viewModelScope.launch {
            _product.value = repository.getProduct(productId)
            refreshComments()
        }
    }

    fun addComment(content: String) {
        if (content.isBlank()) {
            _toastMessage.value = "请输入评论内容"
            return
        }
        viewModelScope.launch {
            repository.addComment(productId, currentUser, content.trim())
            refreshComments()
            _toastMessage.value = "评论成功"
        }
    }

    fun replyComment(parentId: Long, content: String) {
        if (content.isBlank()) {
            _toastMessage.value = "请输入回复内容"
            return
        }
        viewModelScope.launch {
            repository.addComment(productId, currentUser, content.trim(), parentId)
            refreshComments()
            _toastMessage.value = "回复成功"
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    private suspend fun refreshComments() {
        _comments.postValue(repository.getCommentItems(productId))
    }
}
