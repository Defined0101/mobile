package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val repository = CategoryRepository(RetrofitClient.apiService)

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val data = repository.getCategories(forceRefresh)
                _categories.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearCategoryCache() {
        repository.clearCache()
    }
}
