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

    private val _recipeDetails = MutableStateFlow<Recipe?>(null)
    val recipeDetails: StateFlow<Recipe?> = _recipeDetails

    private val _recipeCard = MutableStateFlow<Map<String, Any>?>(null)
    val recipeCard: StateFlow<Map<String, Any>?> = _recipeCard

    init {
        fetchRecipes()
        print(recipes)
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            try {
                //val response = RetrofitClient.apiService.getUserRecommendations()
                //_recipes.value = response

                // Simulating a dummy response with a list of recipes
                _recipes.value = listOf(
                    Recipe(
                        ID = 1,
                        Name = "Pasta Carbonara",
                        Instructions = "Cook pasta and mix with eggs, cheese, pancetta, and pepper.",
                        Ingredients = listOf(),
                        TotalTime = 30f,
                        Calories = 450f,
                        Fat = 12f,
                        Protein = 15f,
                        Carbohydrate = 60f,
                        Category = "Italian",
                        Label = listOf("Vegetarian")
                    ),
                    Recipe(
                        ID = 2,
                        Name = "Grilled Chicken",
                        Instructions = "Grill chicken and serve with vegetables.",
                        Ingredients = listOf(),
                        TotalTime = 20f,
                        Calories = 350f,
                        Fat = 8f,
                        Protein = 30f,
                        Carbohydrate = 15f,
                        Category = "Grilled",
                        Label = listOf("Low Fat")
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            try {
                //val response = RetrofitClient.apiService.getRecipeDetails(recipeId)
                //_recipeDetails.value = response

                val dummyRecipe = Recipe(
                    ID = recipeId,
                    Name = "Recipe $recipeId",
                    Instructions = "Instructions for Recipe $recipeId",
                    Ingredients = listOf(),
                    TotalTime = 45f,
                    Calories = 400f,
                    Fat = 10f,
                    Protein = 20f,
                    Carbohydrate = 50f,
                    Category = "Category $recipeId",
                    Label = listOf("Vegetarian")
                )
                _recipeDetails.value = dummyRecipe
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

    fun searchRecipes(queryJson: String, sortByField: String, sortByDirection: String) {
        viewModelScope.launch {
            try {
                val encodedQuery = URLEncoder.encode(queryJson, "UTF-8") // Ensure safe URL encoding
                val response = RetrofitClient.apiService.queryRecipes(encodedQuery, sortByField, sortByDirection)
                _recipes.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
