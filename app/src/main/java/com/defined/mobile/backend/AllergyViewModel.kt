package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AllergyViewModel : ViewModel() {
    private val _allergies = MutableStateFlow<List<String>>(emptyList())
    val allergies: StateFlow<List<String>> = _allergies

    init {
        fetchAllergies()
    }

    private fun fetchAllergies() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAllergies()
                _allergies.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
