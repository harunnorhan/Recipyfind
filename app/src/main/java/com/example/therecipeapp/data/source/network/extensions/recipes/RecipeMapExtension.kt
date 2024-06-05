package com.example.therecipeapp.data.source.network.extensions.recipes

import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.response.recipes.RecipeResult
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.models.recipes.RecipeModel

fun RecipeResult.toRecipeModel(): RecipeModel {
    return RecipeModel(
        id = id,
        title = title,
        image = image
    )
}

fun RecipesResponse.toRecipesModelList(): List<RecipeModel> {
    return results?.mapNotNull { it?.toRecipeModel() }.orEmpty()
}

fun RecipesResponse.toLocal(): List<LocalRecipes> {
    return results?.map { recipeResult ->
        LocalRecipes(
            id = recipeResult?.id ?: 0,
            title = recipeResult?.title.orEmpty(),
            image = recipeResult?.image.orEmpty()
        )
    }.orEmpty()
}