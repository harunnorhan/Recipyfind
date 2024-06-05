package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>>

    suspend fun getIngredientsById(id: Int): Flow<ApiResult<IngredientsResponse>>

    suspend fun getInstructionsById(id: Int): Flow<ApiResult<List<InstructionsResponse>>>
}