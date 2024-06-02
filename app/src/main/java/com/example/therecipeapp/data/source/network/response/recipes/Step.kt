package com.example.therecipeapp.data.source.network.response.recipes


import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("step")
    val step: String?
)