package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class Recipe (
    @SerializedName("recipe_id") val ID: Int,
    @SerializedName("recipe_name") val Name: String = "",
    @SerializedName("instruction") val Instructions: String = "",
    @SerializedName("ingredients") val Ingredients: List<Ingredient> = emptyList(),
    @SerializedName("total_time") val TotalTime: Float = 0.0f,
    @SerializedName("calories") val Calories: Float = 0.0f,
    @SerializedName("fat") val Fat: Float = 0.0f,
    @SerializedName("protein") val Protein: Float = 0.0f,
    @SerializedName("carb") val Carbohydrate: Float = 0.0f,
    @SerializedName("category") val Category: String = "",
    @SerializedName("label") val Label: List<String>? = listOf()
)

data class RecipeSearchResponse (
    @SerializedName("recipe_id") val ID: Int,
    @SerializedName("recipe_name") val Name: String = "",
    @SerializedName("instruction") val Instructions: String = "",
    @SerializedName("ingredients") val Ingredients: List<String> = emptyList(),
    @SerializedName("total_time") val TotalTime: Float = 0.0f,
    @SerializedName("calories") val Calories: Float = 0.0f,
    @SerializedName("fat") val Fat: Float = 0.0f,
    @SerializedName("protein") val Protein: Float = 0.0f,
    @SerializedName("carb") val Carbohydrate: Float = 0.0f,
    @SerializedName("category") val Category: String = "",
    @SerializedName("label") val Label: List<String>? = listOf()
)

data class QueryClass(
    @SerializedName("inputText") val inputText: String,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("labels") val labels: List<String>
)

data class RecipeSearch(
    @SerializedName("query") val query: QueryClass,
    @SerializedName("sortByField") val sortByField: String,
    @SerializedName("sortByDirection") val sortByDirection: String
)
