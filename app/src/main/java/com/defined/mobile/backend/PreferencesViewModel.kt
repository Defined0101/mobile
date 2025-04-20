package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferencesViewModel : ViewModel() {
    private val repository = PreferencesRepository(RetrofitClient.apiService)

    private val _preferences = MutableStateFlow<List<String>>(emptyList())
    val preferences: StateFlow<List<String>> = _preferences

    init {
        fetchPreferences()
    }

    private fun fetchPreferences(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val data = repository.getPreferences(forceRefresh)
            _preferences.value = data
        }
    }

    fun clearPreferencesCache() {
        repository.clearCache()
    }
}
