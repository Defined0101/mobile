package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("ingr_id") val id: Int,
    @SerializedName("ingr_name") val name: String
)

data class IngredientResponse(
    @SerializedName("items") val items: List<Ingredient> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("page") val page: Int = 1,
    @SerializedName("page_size") val pageSize: Int = 20,
    @SerializedName("total_pages") val totalPages: Int = 1,
    @SerializedName("has_next") val hasNext: Boolean = false,
    @SerializedName("has_previous") val hasPrevious: Boolean = false
)
