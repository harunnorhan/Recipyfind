package com.example.therecipeapp.data.source.network.response.instuctions

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstructionsResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("steps")
    val steps: List<Step?>?
) : Parcelable