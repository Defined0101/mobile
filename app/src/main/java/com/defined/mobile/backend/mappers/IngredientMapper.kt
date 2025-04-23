package com.defined.mobile.backend.mappers

import com.defined.mobile.entities.IngredientDto
import com.defined.mobile.entities.Ingredient

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        name = this.name,
        default_unit = "",
        unit = "gram",
        quantity = 0f,
        available = false
    )
}
