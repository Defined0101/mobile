package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LikedRecipeViewModel : ViewModel() {

    private val _likedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val likedRecipes: StateFlow<List<Recipe>> = _likedRecipes

    fun fetchLikedRecipes(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserLikedRecipes(userId)
                _likedRecipes.value = response

                //println("fetchLikedRecipes: " + response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun likedRecipe(userId: String, recipe: Recipe) {
        val updatedLikedRecipes = _likedRecipes.value.toMutableList()

        if (updatedLikedRecipes.none { it.ID == recipe.ID }) {
            updatedLikedRecipes.add(recipe)
            _likedRecipes.value = updatedLikedRecipes

            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.likeRecipe(userId, recipe)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun removeLikedRecipe(userId: String, recipe: Recipe) {
        val updatedLikedRecipes = _likedRecipes.value.toMutableList()
        if (updatedLikedRecipes.removeAll { it.ID == recipe.ID }) {
            _likedRecipes.value = updatedLikedRecipes
            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.unlikeRecipe(userId, recipe.ID)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}
