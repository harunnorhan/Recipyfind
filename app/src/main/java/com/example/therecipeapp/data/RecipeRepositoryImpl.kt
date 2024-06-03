package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.local.RecipeDao
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.NetworkDataSource
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.data.source.toLocal
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
}