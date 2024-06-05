package com.example.therecipeapp.data.source.network.response.ingredients


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientsResponse(
    @SerializedName("ingredients")
    val ingredients: List<Ingredient?>?
) : Parcelable