package com.defined.mobile.ui

import com.google.firebase.firestore.DocumentReference

data class User(
    val allergies: List<Ingredients>,
    val preferences: Preferences,
    val savedRecipes: List<DocumentReference> //Will be referenced to the id of recipe
)
