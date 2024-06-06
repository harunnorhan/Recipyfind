package com.example.therecipeapp.data.source.network.extensions.informations

import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
import com.example.therecipeapp.models.informations.InformationModel

fun InformationResponse.toInformationModel(): InformationModel {
    return InformationModel(
        id = id ?: 0,
        title = title ?: "no title",
        image = image ?: "no image",
        instructions = instructions ?: "no instructions",
        readyInMinutes = readyInMinutes ?: 0,
        servings = servings ?: 0,
        sourceUrl = sourceUrl ?: "no source url"
    )
}
