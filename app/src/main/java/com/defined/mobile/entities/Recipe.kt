package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class Recipe (
    @SerializedName("recipe_id") val ID: Int,
    @SerializedName("recipe_name") val Name: String,
    @SerializedName("instruction") val Instructions: String,
    @SerializedName("ingredient") val Ingredients: List<Ingredient> = emptyList(),
    @SerializedName("total_time") val TotalTime: Float = 0.0f,
    @SerializedName("calories") val Calories: Float,
    @SerializedName("fat") val Fat: Float,
    @SerializedName("protein") val Protein: Float,
    @SerializedName("carb") val Carbohydrate: Float,
    @SerializedName("category") val Category: String = "kategori",
    @SerializedName("label") val Label: List<String>? = listOf("dairy_free")
)

data class QueryClass(
    @SerializedName("input_text") val inputText: String,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("labels") val labels: List<String>
)

data class RecipeSearch(
    @SerializedName("query") val query: QueryClass,
    @SerializedName("sortBy_field") val sortByField: String,
    @SerializedName("sortBy_direction") val sortByDirection: String
)