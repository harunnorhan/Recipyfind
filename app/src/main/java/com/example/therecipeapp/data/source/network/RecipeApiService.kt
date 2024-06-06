package com.example.therecipeapp.data.source.network

import com.example.therecipeapp.data.source.network.response.informations.InformationResponse
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

    @GET("{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = ApiUtils.API_KEY,
    ): Response<InformationResponse>
}