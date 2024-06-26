package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>>

    suspend fun getRecipeInformation(id: Int): Flow<ApiResult<InformationResponse>>
}