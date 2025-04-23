package com.defined.mobile.backend

import com.defined.mobile.entities.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val apiService: ApiService) {

    // İn-memory cache (başlangıçta boş)
    private var cachedRecipes: List<Recipe>? = null
    private var cachedDislikedRecipes: List<Recipe>? = null
    private var cachedLikedRecipes: List<Recipe>? = null
    private var cachedSavedRecipes: List<Recipe>? = null

    // API çağrısı yapmadan önce cache kontrolü
    suspend fun getRecipes(userId: String, forceRefresh: Boolean = false): List<Recipe> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedRecipes == null) {
                try {
                    cachedRecipes = apiService.getUserRecommendations(userId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedRecipes = emptyList()
                }
            }
            cachedRecipes ?: emptyList()
        }
    }

    suspend fun getDislikedRecipes(userId: String, forceRefresh: Boolean = false): List<Recipe> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedDislikedRecipes == null) {
                try {
                    cachedDislikedRecipes = apiService.getDislikedRecipes(userId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedDislikedRecipes = emptyList()
                }
            }
            cachedDislikedRecipes ?: emptyList()
        }
    }

    suspend fun getLikedRecipes(userId: String, forceRefresh: Boolean = false): List<Recipe> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedLikedRecipes == null) {
                try {
                    cachedLikedRecipes = apiService.getLikedRecipes(userId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedLikedRecipes = emptyList()
                }
            }
            cachedLikedRecipes ?: emptyList()
        }
    }

    suspend fun getSavedRecipes(userId: String, forceRefresh: Boolean = false): List<Recipe> {
        return withContext(Dispatchers.IO) {
            if (forceRefresh || cachedSavedRecipes == null) {
                try {
                    cachedSavedRecipes = apiService.getLikedRecipes(userId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    cachedSavedRecipes = emptyList()
                }
            }
            cachedSavedRecipes ?: emptyList()
        }
    }

    // Arama/sorgu yapılırken cache güncellenebilir
    suspend fun searchRecipes(
        queryJson: String,
        sortByField: String,
        sortByDirection: String,
        forceRefresh: Boolean = false
    ): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                val recipes = apiService.queryRecipes(queryJson, sortByField, sortByDirection)
                // İsteğe bağlı: cache'i güncelle
                cachedRecipes = recipes
                recipes
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Cache temizleme fonksiyonu
    fun clearCache() {
        cachedRecipes = null
        cachedDislikedRecipes = null
        cachedLikedRecipes = null
        cachedSavedRecipes = null
    }
}
