package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.data.source.network.utils.ApiUtils
import com.example.therecipeapp.utils.ApiResult
import com.example.therecipeapp.utils.apiFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeNetworkDataSource @Inject constructor(
    private val recipesApi: RecipeApiService
) : NetworkDataSource {

    override suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>> = apiFlow {
        recipesApi.searchRecipes(
            apiKey = ApiUtils.API_KEY,
            type = type
        )
    }

    override suspend fun getRecipeInformation(id: Int): Flow<ApiResult<InformationResponse>> = apiFlow {
        recipesApi.getRecipeInformation(
            apiKey = ApiUtils.API_KEY,
            id = id
        )
    }
}