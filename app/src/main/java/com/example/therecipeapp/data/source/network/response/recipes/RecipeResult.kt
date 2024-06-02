package com.example.therecipeapp.data.source.network.response.recipes


import com.google.gson.annotations.SerializedName

data class RecipeResult(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient?>?,
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction?>?,
)