package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IngredientsViewModel : ViewModel() {
    private val _ingredients = MutableStateFlow<List<String>>(emptyList())
    val ingredients: StateFlow<List<String>> = _ingredients

    init {
        fetchIngredients()
    }

    fun fetchIngredients() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getIngredients()
                _ingredients.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
