package com.example.therecipeapp.models.informations

import com.example.therecipeapp.models.IngredientModel.IngredientModel
import com.google.gson.annotations.SerializedName

data class InformationModel(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("instructions")
    val instructions: String?,

    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,

    @SerializedName("servings")
    val servings: Int?,

    @SerializedName("sourceUrl")
    val sourceUrl: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("ingredients")
    val ingredients: List<IngredientModel?>?
)