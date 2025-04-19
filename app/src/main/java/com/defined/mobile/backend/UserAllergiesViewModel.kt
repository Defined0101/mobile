package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defined.mobile.entities.UserAllergies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserAllergiesViewModel : ViewModel() {
    private val _userAllergies = MutableStateFlow<UserAllergies?>(null)
    val userAllergies: StateFlow<UserAllergies?> = _userAllergies

    fun fetchUserAllergies(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserAllergies(userId)
                println("Fetched allergies: ${response.allergies}") // Debugging log
                _userAllergies.value = response ?: UserAllergies(userId, emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updateUserAllergies(allergies: UserAllergies) {
        println("allergies: "+ allergies)
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.setUserAllergies(allergies)
                _userAllergies.value = allergies
            } catch (e: HttpException) {
                println("HTTP Error: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
