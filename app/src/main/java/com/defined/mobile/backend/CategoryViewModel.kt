package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                //val response = RetrofitClient.apiService.getCategories()
                //_categories.value = response

                // Simulating a dummy response
                _categories.value = listOf("Fruits", "Vegetables", "Dairy", "Grains", "Meats")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
