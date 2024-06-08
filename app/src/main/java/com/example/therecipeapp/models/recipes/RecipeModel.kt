package com.example.therecipeapp.models.recipes

import com.google.gson.annotations.SerializedName

data class RecipeModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("isFavorited")
    var isFavorited: Boolean = false
)