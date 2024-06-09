package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.local.RecipeDao
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.NetworkDataSource
import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
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

    override suspend fun getRecipeInformation(id: Int): Flow<ApiResult<InformationResponse>> {
        return networkDataSource.getRecipeInformation(id)
    }

    override suspend fun addRecipeToFavorites(recipe: LocalRecipes) {
        localDataSource.insertRecipe(recipe)
    }

    override suspend fun getLocalRecipes(): Flow<List<LocalRecipes>> {
        return localDataSource.observeRecipes()
    }

    override suspend fun isRecipeFavorited(id: Int): Boolean {
        return localDataSource.getRecipeById(id) != null
    }

    override suspend fun removeRecipeFromFavorites(id: Int) {
        localDataSource.deleteRecipeById(id)
    }

    override suspend fun deleteRecipe(id: Int) {
        localDataSource.deleteRecipeById(id)
    }

    override suspend fun clearAllFavorites() {
        localDataSource.clearAllRecipes()
    }

    override suspend fun getAllRecipeIds() {
        localDataSource.getAllRecipeIds()
    }

}