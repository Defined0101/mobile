package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferencesViewModel : ViewModel() {
    private val _preferences = MutableStateFlow<List<String>>(emptyList())
    val preferences: StateFlow<List<String>> = _preferences

    init {
        //fetchPreferences()
    }

    private fun fetchPreferences() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getPreferences()
                _preferences.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
