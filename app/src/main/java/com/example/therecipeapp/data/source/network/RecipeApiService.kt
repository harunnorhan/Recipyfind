package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
import com.example.therecipeapp.data.source.network.response.recipes.RecipesResponse
import com.example.therecipeapp.data.source.network.utils.ApiUtils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("complexSearch?")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = ApiUtils.API_KEY,
        @Query("type") type: String,
    ): Response<RecipesResponse>

    @GET("{id}/ingredientWidget.json")
    suspend fun getIngredientsById(
        @Query("apiKey") apiKey: String = ApiUtils.API_KEY,
        @Path("id") id: Int,
    ): Response<IngredientsResponse>

    @GET("{id}/analyzedInstructions")
    suspend fun getInstructionsById(
        @Query("apiKey") apiKey: String = ApiUtils.API_KEY,
        @Path("id") id: Int,
    ): Response<List<InstructionsResponse>>
}