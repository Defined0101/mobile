package com.defined.mobile.backend

import com.defined.mobile.entities.Recipe
import com.defined.mobile.entities.UserPreferences
import com.defined.mobile.entities.UserAllergies
import com.defined.mobile.entities.UserIngredients
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @GET("getCategories")
    suspend fun getCategories(): List<String>

    @GET("getUserRecommendations")
    suspend fun getUserRecommendations(): List<Recipe>

    @GET("getRecipeDetails")
    suspend fun getRecipeDetails(@Query("recipe_id") recipeId: Int): Recipe

    @GET("getRecipeCard")
    suspend fun getRecipeCard(
        @Query("recipe_id") recipeId: Int,
        @Query("fields") fields: List<String>
    ): Map<String, List<String>>

    @GET("getPreferences") // was getLabels. to avoid confusion changed to Preferences = Labels.
    suspend fun getPreferences(): List<String>

    @GET("getUserPreferences")
    suspend fun getUserPreferences(@Query("user_id") userId: String): UserPreferences

    @POST("setUserPreferences")
    suspend fun setUserPreferences(@Body preferences: UserPreferences)

    @GET("query")
    suspend fun queryRecipes(
        @Query("query") queryJson: String, // JSON-encoded query
        @Query("sortBy.field") sortByField: String,
        @Query("sortBy.direction") sortByDirection: String
    ): List<Recipe>

    @GET("getAllergies")
    suspend fun getAllergies(): List<String>

    @GET("getUserAllergies")
    suspend fun getUserAllergies(@Query("user_id") userId: String): UserAllergies

    @POST("setUserAllergies")
    suspend fun setUserAllergies(@Body allergies: UserAllergies)

    @GET("getIngredients")
    suspend fun getIngredients(): List<String>

    @GET("getUserIngredients")
    suspend fun getUserIngredients(@Query("user_id") userId: String): UserIngredients

    @POST("setUserAllergies")
    suspend fun setUserIngredients(@Body ingredients: UserIngredients)
}
