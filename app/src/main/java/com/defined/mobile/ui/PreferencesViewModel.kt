package com.defined.mobile.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PreferencesViewModel : ViewModel() {
    var isDairyFree = mutableStateOf(false)
    var isGlutenFree = mutableStateOf(false)
    var isPescetarian = mutableStateOf(false)
    var isVegan = mutableStateOf(false)
    var isVegetarian = mutableStateOf(false)

    // Save changes locally (could also write to persistent storage)
    fun savePreferences(
        dairyFree: Boolean,
        glutenFree: Boolean,
        pescetarian: Boolean,
        vegan: Boolean,
        vegetarian: Boolean
    ) {
        isDairyFree.value = dairyFree
        isGlutenFree.value = glutenFree
        isPescetarian.value = pescetarian
        isVegan.value = vegan
        isVegetarian.value = vegetarian
    }
}
