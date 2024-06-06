package com.example.therecipeapp.feature.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
import com.example.therecipeapp.feature.error.ErrorView
import com.example.therecipeapp.feature.loading.LoadingView
import com.example.therecipeapp.models.ingredients.IngredientModel
import com.example.therecipeapp.models.recipes.RecipeModel

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    id: Int,
    title: String,
    image: String
) {
    LaunchedEffect(id, title, image) {
        viewModel.loadRecipeData(
            recipeId = id,
            recipeTitle = title,
            recipeImage = image
        )
    }

    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> LoadingView()
            state.isError -> ErrorView()
            state.recipe != null -> RecipeDetailView(state.recipe!!, state.ingredients /*, state.instructions*/)
        }
    }
}

@Composable
fun RecipeDetailView(recipe: RecipeModel, ingredients: List<IngredientModel>?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = recipe.title ?: "No title", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))

        ingredients?.let {
            Text(text = "Ingredients:")
            it.forEach { ingredient ->
                Text(text = "${ingredient.name}: ${ingredient.amount} ${ingredient.unit} ${ingredient.image}")
            }
        }

        /*
        instructions?.firstOrNull()?.steps?.let {
            Text(text = "Instructions:")
            it.forEach { step ->
                Text(text = "${step?.number}. ${step?.step}")
            }
        }

         */
    }
}