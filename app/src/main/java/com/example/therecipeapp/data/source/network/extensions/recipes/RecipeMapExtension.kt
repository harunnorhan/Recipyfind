package com.example.therecipeapp.data.source.network.extensions.recipes

import com.example.therecipeapp.data.source.network.response.recipes.RecipeResult
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.models.recipes.RecipeModel

fun RecipeResult.toRecipeModel(): RecipeModel {
    return RecipeModel(
        id = id ?: 0,
        title = title,
        image = image
    )
}

fun RecipesResponse.toRecipesModelList(): List<RecipeModel> {
    return results?.mapNotNull { it?.toRecipeModel() }.orEmpty()
}