package com.example.therecipeapp.models.recipes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    var id: Int?,
    var title: String?,
    var image: String?
) : Parcelable