package com.example.therecipeapp.models.recipes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    val id: Int,
    val title: String?,
    val image: String?
) : Parcelable