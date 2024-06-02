package com.example.therecipeapp.data.source

import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.response.recipes.RecipeResult
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.models.RecipesModel

fun RecipesResponse.toLocal(): List<LocalRecipes> {
    return this.results?.map {
        it?.toLocal() ?: LocalRecipes(
            id = 0,
            title = "",
            image = "",
            servings = 0,
            readyInMinutes = 0,
            //extendedIngredients = null,
            //analyzedInstructions = null
        )
    }.orEmpty()
}

fun RecipeResult.toLocal(): LocalRecipes {
    return LocalRecipes(
        id = this.id ?: 0,
        title = this.title ?: "",
        image = this.image ?: "",
        servings = this.servings ?: 0,
        readyInMinutes = this.readyInMinutes ?: 0,
        //extendedIngredients = this.extendedIngredients.orEmpty(),
        //analyzedInstructions = this.analyzedInstructions.orEmpty()
    )
}

fun LocalRecipes.toModel(): RecipesModel {
    return RecipesModel(
        id = this.id,
        title = this.title,
        image = this.image,
        servings = this.servings,
        readyInMinutes = readyInMinutes
        //extendedIngredients = this.extendedIngredients,
        //analyzedInstructions = this.analyzedInstructions
    )
}