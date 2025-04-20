package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class Recipe (
    val ID: Int,
    val Name: String,
    val Instructions: String,
    val Ingredients: List<Ingredient> = emptyList(),
    @SerializedName("TotalTime") val TotalTime: Float = 0.0f,
    val Calories: Float,
    val Fat: Float,
    val Protein: Float,
    val Carbohydrate: Float,
    val Category: String,
    val Label: List<String> = emptyList()
)
