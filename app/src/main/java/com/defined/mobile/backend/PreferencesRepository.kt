package com.defined.mobile.backend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepository(private val apiService: ApiService) {
    private var cachedPreferences: List<String>? = null

    suspend fun getPreferences(forceRefresh: Boolean = false): List<String> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedPreferences == null) {
                try {
                    cachedPreferences = apiService.getPreferences()
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedPreferences = emptyList()
                }
            }
            cachedPreferences ?: emptyList()
        }
    }

    fun clearCache() {
        cachedPreferences = null
    }
}
