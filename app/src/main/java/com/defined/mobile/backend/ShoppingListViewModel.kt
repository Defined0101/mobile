package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.defined.mobile.entities.Ingredient

class ShoppingListViewModel : ViewModel() {
    private val _shoppingList = MutableStateFlow<List<Ingredient>>(emptyList())
    val shoppingList: StateFlow<List<Ingredient>> = _shoppingList

    fun addIngredients(ingredients: List<Ingredient>) {
        _shoppingList.value = _shoppingList.value.toMutableList().apply {
            addAll(ingredients)
        }
    }

    fun clearList() {
        _shoppingList.value = emptyList()
    }
}
