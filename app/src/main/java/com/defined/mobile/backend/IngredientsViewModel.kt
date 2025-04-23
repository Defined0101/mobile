package com.defined.mobile.backend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.defined.mobile.entities.IngredientDto
import com.defined.mobile.backend.mappers.toDomain
import com.defined.mobile.entities.Ingredient
import com.defined.mobile.entities.IngredientResponse

class IngredientsViewModel : ViewModel() {
    private val _ingredients = MutableStateFlow(IngredientResponse())
    val ingredients: StateFlow<IngredientResponse> = _ingredients

    private var currentPage = 1
    private var isLoading = false
    private var hasNextPage = true
    private var currentSearch = ""

    init {
        fetchIngredients()
    }

    fun fetchIngredients(reset: Boolean = false, search: String = "") {
        if (isLoading || (!hasNextPage && !reset)) return

        viewModelScope.launch {
            isLoading = true
            if (reset) {
                currentPage = 1
                hasNextPage = true
                _ingredients.value = IngredientResponse()
            }
            try {
                val searchParam = if (search.isBlank()) null else search

                val response = RetrofitClient.apiService.getIngredients(
                    page = currentPage,
                    pageSize = 10,
                    search = searchParam
                )
                val newItems = response.items.map { it }
                val combinedItems = _ingredients.value.items + newItems
                _ingredients.value = response.copy(items = combinedItems)
                hasNextPage = response.hasNext
                currentPage++
                currentSearch = search
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun onSearchChanged(query: String) {
        if (query != currentSearch) {
            fetchIngredients(reset = true, search = query)
        }
    }

    fun loadNextPage() {
        fetchIngredients(search = currentSearch)
    }
}
