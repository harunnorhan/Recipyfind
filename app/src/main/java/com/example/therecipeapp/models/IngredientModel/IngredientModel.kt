package com.example.therecipeapp.models.IngredientModel

import com.google.gson.annotations.SerializedName

class IngredientModel (
    @SerializedName("name")
    val name: String?,

    @SerializedName("original")
    val original: String?,
)