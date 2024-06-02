package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>>
}