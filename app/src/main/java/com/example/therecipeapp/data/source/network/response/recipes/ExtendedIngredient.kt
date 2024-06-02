package com.example.therecipeapp.data.source.network.response.recipes


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("original")
    val original: String?,
    @SerializedName("image")
    val image: String?,
)