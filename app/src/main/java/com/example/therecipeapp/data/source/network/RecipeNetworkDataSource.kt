package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
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

    override suspend fun getIngredientsById(id: Int): Flow<ApiResult<IngredientsResponse>> = apiFlow {
        recipesApi.getIngredientsById(
            apiKey = ApiUtils.API_KEY,
            id = id
        )
    }

    override suspend fun getInstructionsById(id: Int): Flow<ApiResult<List<InstructionsResponse>>> = apiFlow {
        recipesApi.getInstructionsById(
            apiKey = ApiUtils.API_KEY,
            id = id
        )
    }
}