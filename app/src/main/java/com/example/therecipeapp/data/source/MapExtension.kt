package com.example.therecipeapp.data.source

import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.response.recipes.AnalyzedInstruction
import com.example.therecipeapp.data.source.network.response.recipes.ExtendedIngredient
import com.example.therecipeapp.data.source.network.response.recipes.RecipeResult
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.data.source.network.response.recipes.Step
import com.example.therecipeapp.models.AnalyzedInstructionModel
import com.example.therecipeapp.models.ExtendedIngredientModel
import com.example.therecipeapp.models.RecipeModel
import com.example.therecipeapp.models.StepModel

fun RecipeResult.toRecipeModel(): RecipeModel {
    return RecipeModel(
        id = id,
        title = title,
        image = image,
        servings = servings,
        readyInMinutes = readyInMinutes,
        extendedIngredients = extendedIngredients?.map { it.toExtendedIngredientModel() }.orEmpty(),
        analyzedInstructions = analyzedInstructions?.map { it.toAnalyzedInstructionModel() }.orEmpty()
    )
}

fun ExtendedIngredient?.toExtendedIngredientModel(): ExtendedIngredientModel {
    return ExtendedIngredientModel(
        id = this?.id,
        name = this?.name,
        original = this?.original,
        image = this?.image
    )
}

fun AnalyzedInstruction?.toAnalyzedInstructionModel(): AnalyzedInstructionModel {
    return AnalyzedInstructionModel(
        name = this?.name,
        steps = this?.steps?.map { it.toStepModel() }.orEmpty()
    )
}

fun Step?.toStepModel(): StepModel {
    return StepModel(
        number = this?.number,
        step = this?.step
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
            image = recipeResult?.image.orEmpty(),
            servings = recipeResult?.servings ?: 0,
            readyInMinutes = recipeResult?.readyInMinutes ?: 0
        )
    }.orEmpty()
}