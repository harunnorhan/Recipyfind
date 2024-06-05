package com.example.therecipeapp.data.source.network.response.ingredients


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Amount(
    @SerializedName("us")
    val us: Us?
): Parcelable