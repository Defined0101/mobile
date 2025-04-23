package com.defined.mobile.backend

import com.defined.mobile.entities.IngredientResponse
import com.defined.mobile.entities.Recipe
import com.defined.mobile.entities.UserPreferences
import com.defined.mobile.entities.UserAllergies
import com.defined.mobile.entities.UserIngredients
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface ApiService {
    @GET("getCategories")
    suspend fun getCategories(): List<String>

    @GET("getUserRecommendations")
    suspend fun getUserRecommendations(@Query("user_id") userId: String): List<Recipe>

    @GET("getRecipeDetails")
    suspend fun getRecipeDetails(@Query("recipe_id") recipeId: Int): Recipe

    @GET("getSurpriseRecipeId")
    suspend fun getSurpriseRecipeId(@Query("user_id") userId: String): Int

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
    suspend fun getIngredients(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String? = null
    ): IngredientResponse

    @GET("getUserIngredients")
    suspend fun getUserIngredients(@Query("user_id") userId: String): UserIngredients

    @POST("setUserIngredients")
    suspend fun setUserIngredients(@Body ingredients: UserIngredients)

    @GET("getUserLikedRecipes")
    suspend fun getUserLikedRecipes(@Query("user_id") userId: String): List<Recipe>

    @POST("likeRecipe")
    suspend fun likeRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @DELETE("unlikeRecipe")
    suspend fun unlikeRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @GET("getUserSavedRecipes")
    suspend fun getUserSavedRecipes(@Query("user_id") userId: String): List<Recipe>

    @POST("saveRecipe")
    suspend fun saveRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @DELETE("unsaveRecipe")
    suspend fun unsaveRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @GET("getUserDislikedRecipes")
    suspend fun getUserDislikedRecipes(@Query("user_id") userId: String): List<Recipe>

    @POST("dislikeRecipe")
    suspend fun dislikeRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @DELETE("unDislikeRecipe")
    suspend fun unDislikeRecipe(
        @Query("user_id") userId: String,
        @Query("recipe_id") recipeId: Int
    )

    @GET("getLikedRecipes")
    suspend fun getLikedRecipes(@Query("user_id") userId: String): List<Recipe>

    @GET("getDislikedRecipes")
    suspend fun getDislikedRecipes(@Query("user_id") userId: String): List<Recipe>
}
