package com.defined.mobile.ui

import androidx.collection.FloatIntMap

data class Recipes(
    val name: String,
    val instruction: String,
    val totalTime: Int, //TODO: Check the type
    val ingr: List<Ingredients>, //TODO: Fix ingr data type
    val fat: Float,
    val protein: Float,
    val carb: Float,
    val calories: Float
)
