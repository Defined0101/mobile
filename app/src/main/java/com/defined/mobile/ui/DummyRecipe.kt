package com.defined.mobile.ui

data class DummyRecipe (
    val name: String,
    val ingredients: List<String>,
    val mealType: List<String>, // e.g., Breakfast, Lunch, Dinner, Dessert
    val prepTime: Int //minutes
)