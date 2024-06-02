package com.example.therecipeapp.data.source.network.response.recipes


import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("results")
    val results: List<RecipeResult?>?,
)