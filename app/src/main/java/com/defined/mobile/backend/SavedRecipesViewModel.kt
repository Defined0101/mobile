package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedRecipeViewModel : ViewModel() {

    private val _savedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val savedRecipes: StateFlow<List<Recipe>> = _savedRecipes

    fun fetchSavedRecipes(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserSavedRecipes(userId)
                _savedRecipes.value = response

                //println("fetchLikedRecipes: " + response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun savedRecipe(userId: String, recipe: Recipe) {
        val updatedSavedRecipes = _savedRecipes.value.toMutableList()

        if (updatedSavedRecipes.none { it.ID == recipe.ID }) {
            updatedSavedRecipes.add(recipe)
            _savedRecipes.value = updatedSavedRecipes

            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.saveRecipe(userId, recipe)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun removeSavedRecipe(userId: String, recipe: Recipe) {
        val updatedSavedRecipes = _savedRecipes.value.toMutableList()
        if (updatedSavedRecipes.removeAll { it.ID == recipe.ID }) {
            _savedRecipes.value = updatedSavedRecipes
            viewModelScope.launch {
                try {
                    RetrofitClient.apiService.unsaveRecipe(userId, recipe.ID)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}
