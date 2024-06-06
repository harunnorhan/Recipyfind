package com.example.therecipeapp.data.source.network.response.informations


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(
    @SerializedName("nameClean")
    val nameClean: String?
)