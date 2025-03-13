package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserPreferencesViewModel : ViewModel() {
    private val _userPreferences = MutableStateFlow<UserPreferences?>(null)
    val userPreferences: StateFlow<UserPreferences?> = _userPreferences

    fun fetchUserPreferences(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserPreferences(userId)
                println("Full API response: $response") // Debugging log
                println("Fetched preferences: ${response.preferences}") // Debugging log
                _userPreferences.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateUserPreferences(preferences: UserPreferences) {
        println("preferences: " + preferences)
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.setUserPreferences(preferences)
                _userPreferences.value = preferences // Update local state after API call
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
