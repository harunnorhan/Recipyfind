package com.example.therecipeapp.models

import com.example.therecipeapp.data.source.network.response.recipes.AnalyzedInstruction
import com.example.therecipeapp.data.source.network.response.recipes.ExtendedIngredient

data class RecipesModel(
    var id: Int?,
    var title: String?,
    var image: String?,
    var servings: Int?,
    var readyInMinutes: Int?,
    //var extendedIngredients: List<ExtendedIngredient?>?,
    //var analyzedInstructions: List<AnalyzedInstruction?>?
)