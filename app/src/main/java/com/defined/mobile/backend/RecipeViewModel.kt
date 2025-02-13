package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        fetchRecipes()
        print(recipes)
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserRecommendations()
                _recipes.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
