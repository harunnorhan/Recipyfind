package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.local.RecipeDao
import com.example.therecipeapp.data.source.network.NetworkDataSource
import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: RecipeDao
): RecipeRepository {
    override suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>> {
        return networkDataSource.searchRecipes(type)
    }

    override suspend fun getIngredientsById(id: Int): Flow<ApiResult<IngredientsResponse>> {
        return networkDataSource.getIngredientsById(id)
    }

    override suspend fun getInstructionsById(id: Int): Flow<ApiResult<List<InstructionsResponse>>> {
        return networkDataSource.getInstructionsById(id)
    }
}