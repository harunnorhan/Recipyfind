package com.example.therecipeapp.models

data class RecipeModel(
    var id: Int?,
    var title: String?,
    var image: String?,
    var servings: Int?,
    var readyInMinutes: Int?,
    var extendedIngredients: List<ExtendedIngredientModel> = emptyList(),
    var analyzedInstructions: List<AnalyzedInstructionModel> = emptyList()
)