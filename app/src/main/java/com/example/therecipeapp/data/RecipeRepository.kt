package com.example.therecipeapp.data

import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun searchRecipes(type: String): Flow<ApiResult<RecipesResponse>>

    suspend fun getRecipeInformation(id: Int): Flow<ApiResult<InformationResponse>>

    suspend fun addRecipeToFavorites(recipe: LocalRecipes)

    suspend fun isRecipeFavorited(id: Int): Boolean

    suspend fun getLocalRecipes(): Flow<List<LocalRecipes>>

    suspend fun removeRecipeFromFavorites(id: Int)

    suspend fun deleteRecipe(id: Int)

    suspend fun clearAllFavorites()
}