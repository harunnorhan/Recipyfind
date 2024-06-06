package com.example.therecipeapp.data.source.network.response.informations

import com.google.gson.annotations.SerializedName

data class InformationResponse(
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

    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient?>?,
)