package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _dislikedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val dislikedRecipes: StateFlow<List<Recipe>> = _dislikedRecipes

    private val _likedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val likedRecipes: StateFlow<List<Recipe>> = _likedRecipes

    private val _savedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val savedRecipes: StateFlow<List<Recipe>> = _savedRecipes

    private val _recipeDetails = MutableStateFlow<Recipe?>(null)
    val recipeDetails: StateFlow<Recipe?> = _recipeDetails

    private val _surpriseRecipe = MutableStateFlow<Int>(0)
    val surpriseRecipe: StateFlow<Int> = _surpriseRecipe

    private val _recipeCard = MutableStateFlow<Map<String, Any>?>(null)
    val recipeCard: StateFlow<Map<String, Any>?> = _recipeCard

    private val repository = RecipeRepository(RetrofitClient.apiService)

    /*
    init {
        fetchRecipes()
        print(recipes)
    }
     */

    fun fetchRecipes(userId: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val recipesList = repository.getRecipes(userId, forceRefresh)
                println("recipesList: " + recipesList)
                _recipes.value = recipesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchLikedRecipes(userId: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val recipesList = repository.getLikedRecipes(userId, forceRefresh)
                _dislikedRecipes.value = recipesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchDislikedRecipes(userId: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val recipesList = repository.getDislikedRecipes(userId, forceRefresh)
                _likedRecipes.value = recipesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchSavedRecipes(userId: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val recipesList = repository.getSavedRecipes(userId, forceRefresh)
                _savedRecipes.value = recipesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecipeDetails(recipeId)
                _recipeDetails.value = response

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchSurpriseRecipeId(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getSurpriseRecipeId(userId)
                _surpriseRecipe.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecipeCard(recipeId: Int, fields: List<String>) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecipeCard(recipeId, fields)
                _recipeCard.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchRecipes(queryJson: String, sortByField: String, sortByDirection: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val encodedQuery = URLEncoder.encode(queryJson, "UTF-8") // Ensure safe URL encoding
                val recipesList = repository.searchRecipes(encodedQuery, sortByField, sortByDirection, forceRefresh)
                _recipes.value = recipesList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearCache() {
        repository.clearCache()
    }
}
