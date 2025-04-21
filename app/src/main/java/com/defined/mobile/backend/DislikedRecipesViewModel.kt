package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DislikedRecipeViewModel : ViewModel() {

    private val _dislikedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val dislikedRecipes: StateFlow<List<Recipe>> = _dislikedRecipes

    fun fetchDislikedRecipes(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserDislikedRecipes(userId)
                _dislikedRecipes.value = response

                //println("fetchLikedRecipes: " + response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addDislikedRecipe(userId: String, recipe: Recipe) {
        val updatedDislikedRecipes = _dislikedRecipes.value.toMutableList()

        if (updatedDislikedRecipes.none { it.ID == recipe.ID }) {
            updatedDislikedRecipes.add(recipe)
            _dislikedRecipes.value = updatedDislikedRecipes

            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.dislikeRecipe(userId, recipe.ID)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun removeDislikedRecipe(userId: String, recipe: Recipe) {
        val updatedDislikedRecipes = _dislikedRecipes.value.toMutableList()
        if (updatedDislikedRecipes.removeAll { it.ID == recipe.ID }) {
            _dislikedRecipes.value = updatedDislikedRecipes
            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.unDislikeRecipe(userId, recipe.ID)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}
