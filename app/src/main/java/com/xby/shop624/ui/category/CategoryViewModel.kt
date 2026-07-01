package com.xby.shop624.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xby.shop624.Shop624App
import com.xby.shop624.data.local.entity.PrimaryCategoryEntity
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.data.local.entity.SubCategoryEntity
import com.xby.shop624.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as Shop624App).categoryRepository

    private val _primaryCategories = MutableLiveData<List<PrimaryCategoryEntity>>()
    val primaryCategories: LiveData<List<PrimaryCategoryEntity>> = _primaryCategories

    private val _subCategories = MutableLiveData<List<SubCategoryEntity>>()
    val subCategories: LiveData<List<SubCategoryEntity>> = _subCategories

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _selectedPrimaryKey = MutableLiveData<String>()
    val selectedPrimaryKey: LiveData<String> = _selectedPrimaryKey

    private val _selectedSubKey = MutableLiveData("all")
    val selectedSubKey: LiveData<String> = _selectedSubKey

    private val _emptyHint = MutableLiveData<String?>()
    val emptyHint: LiveData<String?> = _emptyHint

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val primary = repository.getPrimaryCategories()
            _primaryCategories.value = primary
            if (primary.isNotEmpty()) {
                selectPrimary(primary.first().categoryKey)
            }
        }
    }

    fun selectPrimary(categoryKey: String) {
        if (_selectedPrimaryKey.value == categoryKey) return
        _selectedPrimaryKey.value = categoryKey
        viewModelScope.launch {
            val subs = repository.getSubCategories(categoryKey)
            _subCategories.value = subs
            val firstSub = subs.firstOrNull()?.subKey ?: "all"
            selectSub(firstSub)
        }
    }

    fun selectSub(subKey: String) {
        _selectedSubKey.value = subKey
        val primaryKey = _selectedPrimaryKey.value ?: return
        viewModelScope.launch {
            val list = repository.getProducts(primaryKey, subKey)
            _products.value = list
            _emptyHint.value = if (list.isEmpty()) "该分类暂无商品" else null
        }
    }
}
