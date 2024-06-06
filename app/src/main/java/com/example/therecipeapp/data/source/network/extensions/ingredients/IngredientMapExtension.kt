package com.example.therecipeapp.data.source.network.extensions.ingredients

import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.models.ingredients.IngredientModel

fun IngredientsResponse.toIngredientModelList(): List<IngredientModel> {
    return ingredients?.mapNotNull { ingredient ->
        ingredient?.let {
            IngredientModel(
                name = it.name.orEmpty(),
                amount = it.amount?.us?.value ?: 0.0,
                unit = it.amount?.us?.unit.orEmpty(),
                image = it.image.orEmpty()
            )
        }
    }.orEmpty()
}