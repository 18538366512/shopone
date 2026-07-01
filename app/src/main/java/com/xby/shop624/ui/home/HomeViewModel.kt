package com.xby.shop624.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.BannerEntity
import com.xby.shop624.data.local.entity.NavCategoryEntity
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.data.repository.HomeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val homeRepository = (application as Shop624App).homeRepository

    private val _banners = MutableLiveData<List<BannerEntity>>()
    val banners: LiveData<List<BannerEntity>> = _banners

    private val _navCategories = MutableLiveData<List<NavCategoryEntity>>()
    val navCategories: LiveData<List<NavCategoryEntity>> = _navCategories

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _selectedCategoryKey = MutableLiveData(HomeRepository.CATEGORY_ALL)
    val selectedCategoryKey: LiveData<String> = _selectedCategoryKey

    private val _searchKeyword = MutableLiveData("")
    val searchKeyword: LiveData<String> = _searchKeyword

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _emptyHint = MutableLiveData<String?>()
    val emptyHint: LiveData<String?> = _emptyHint

    private var searchJob: Job? = null

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _banners.value = homeRepository.getBanners()
            _navCategories.value = homeRepository.getNavCategories()
            refreshProducts()
            _isLoading.value = false
        }
    }

    fun selectCategory(categoryKey: String) {
        if (_selectedCategoryKey.value == categoryKey) return
        _selectedCategoryKey.value = categoryKey
        viewModelScope.launch {
            refreshProducts()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchKeyword.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            refreshProducts()
        }
    }

    private suspend fun refreshProducts() {
        val keyword = _searchKeyword.value.orEmpty().trim()
        val categoryKey = _selectedCategoryKey.value
        val result = if (keyword.isEmpty()) {
            homeRepository.getProducts(categoryKey)
        } else {
            homeRepository.searchProducts(keyword, categoryKey)
        }
        _products.postValue(result)
        _emptyHint.postValue(
            if (result.isEmpty()) {
                if (keyword.isNotEmpty()) "未找到「$keyword」相关商品" else "该分类暂无商品"
            } else {
                null
            }
        )
    }
}
