package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("name") val name: String,
    @SerializedName("default_unit") val default_unit: String = "",
    @SerializedName("unit") val unit: String,
    @SerializedName("quantity") val quantity: Float,
    @SerializedName("available") var available: Boolean = false
)

data class IngredientSearch(
    @SerializedName("page") val page: Int,
    @SerializedName("page_size") val page_size: Int,
    @SerializedName("search") val search: String
)
