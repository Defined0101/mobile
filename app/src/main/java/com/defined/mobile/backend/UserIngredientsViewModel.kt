package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.UserIngredients
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserIngredientsViewModel : ViewModel() {
    private val _userIngredients = MutableStateFlow<UserIngredients?>(null)
    val userIngredients: StateFlow<UserIngredients?> = _userIngredients

    fun fetchUserIngredients(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserIngredients(userId)
                println("Fetched ingredients: ${response.ingredients}") // Debugging log
                _userIngredients.value = response ?: UserIngredients(userId, emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUserIngredients(ingredients: UserIngredients) {
        // Log the ingredients data
        println("Updating ingredients: $ingredients")

        // Launching the coroutine to update user ingredients in a background thread
        viewModelScope.launch {
            try {
                // Check if the ingredients are different from the current ones before making a network call
                if (_userIngredients.value != ingredients) {
                    RetrofitClient.apiService.setUserIngredients(ingredients)
                    _userIngredients.value = ingredients
                    println("Successfully updated ingredients for user: ${ingredients.userId}")
                } else {
                    println("No changes detected in ingredients. Skipping update.")
                }
            } catch (e: HttpException) {
                println("HTTP Error: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error updating ingredients: ${e.localizedMessage}")
            }
        }
    }

}
