package com.example.therecipeapp.data.source.network.response.ingredients


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    @SerializedName("amount")
    val amount: Amount?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable