package com.example.therecipeapp.data.source.local

import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.models.informations.InformationModel

fun InformationModel.toLocalRecipe(): LocalRecipes {
    return LocalRecipes(
        id = id ?: 0,
        image = image ?: "no image",
        title = title ?: "no title"
    )
}