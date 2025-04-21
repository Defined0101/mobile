package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.defined.mobile.entities.Ingredient

class ShoppingListViewModel : ViewModel() {
    private val _shoppingList = MutableStateFlow<List<Ingredient>>(emptyList())
    val shoppingList: StateFlow<List<Ingredient>> = _shoppingList

    fun addIngredients(ingredients: List<Ingredient>) {
        val currentList = _shoppingList.value
        // collect the set of existing ingredient names
        val existingNames = currentList.map { it.name }.toSet()

        // filter out any incoming ingredients whose name is already in the list
        val toAdd = ingredients.filter { it.name !in existingNames }

        // only append the truly-new ones
        _shoppingList.value = currentList + toAdd
    }

    fun removeIngredient(ingredient: Ingredient) {
        _shoppingList.value = _shoppingList.value
            .toMutableList()
            .apply { remove(ingredient) }
    }

    fun clearList() {
        _shoppingList.value = emptyList()
    }
}
