package com.example.therecipeapp.data.source.network.response.recipes


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipesResponse(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("results")
    val results: List<RecipeResult?>?
) : Parcelable