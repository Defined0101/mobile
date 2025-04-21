package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class UserIngredients (
    @SerializedName("user_id") val userId: String,
    val ingredients: List<Ingredient>
)
