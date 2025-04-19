package com.defined.mobile.entities

import com.google.gson.annotations.SerializedName

data class Preferences(
    @SerializedName("dairy_free") val dairyFree: Boolean,
    @SerializedName("gluten_free") val glutenFree: Boolean,
    @SerializedName("pescetarian") val pescetarian: Boolean,
    @SerializedName("vegan") val vegan: Boolean,
    @SerializedName("vegetarian") val vegetarian: Boolean
)

data class UserPreferences(
    @SerializedName("user_id") val userId: String,
    @SerializedName("preferences") val preferences: Preferences
)
