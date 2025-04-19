package com.defined.mobile.backend

import com.defined.mobile.entities.Recipe
import retrofit2.http.GET

interface ApiService {
    @GET("getCategories")
    suspend fun getCategories(): List<String>

    @GET("getUserRecommendations")
    suspend fun getUserRecommendations(): List<Recipe>
}
