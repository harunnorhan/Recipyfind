package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.data.source.network.utils.ApiUtils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("instructionsRequired=true&fillIngredients=true&addRecipeInformation=true")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = ApiUtils.API_KEY,
        @Query("type") type: String,
    ): Response<RecipesResponse>
}