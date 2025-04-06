package com.defined.mobile.backend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val apiService: ApiService) {
    private var cachedCategories: List<String>? = null

    suspend fun getCategories(forceRefresh: Boolean = false): List<String> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedCategories == null) {
                try {
                    cachedCategories = apiService.getCategories()
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedCategories = emptyList()
                }
            }
            cachedCategories ?: emptyList()
        }
    }

    fun clearCache() {
        cachedCategories = null
    }
}
