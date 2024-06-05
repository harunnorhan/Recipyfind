package com.example.therecipeapp.feature.recipe

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.network.response.ingredients.IngredientsResponse
import com.example.therecipeapp.data.source.network.response.instuctions.InstructionsResponse
import com.example.therecipeapp.models.recipes.RecipeModel
import com.example.therecipeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val recipe: RecipeModel? = null,
    val ingredients: IngredientsResponse? = null,
    val instructions: List<InstructionsResponse>? = null
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeState())
    val uiState: StateFlow<RecipeState> = _uiState

    fun loadRecipeData(recipe: RecipeModel?) {
        if (recipe != null) {
            Log.d("RecipeViewModel", "Loaded recipe with id: ${recipe.id}")
            _uiState.value = RecipeState(
                recipe = RecipeModel(id = recipe.id, title = recipe.title, image = recipe.image)
            )
            fetchDetails(recipe)
        } else {
            Log.e("RecipeViewModel", "Error: Recipe not found in SavedStateHandle")
            _uiState.value = RecipeState(isError = true)
        }
    }

    private fun fetchDetails(recipe: RecipeModel?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val ingredientsResult = recipe?.id?.let { repository.getIngredientsById(it).first() }
                val instructionsResult = recipe?.id?.let { repository.getInstructionsById(it).first() }
                if (ingredientsResult is ApiResult.Success && instructionsResult is ApiResult.Success) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ingredients = ingredientsResult.data,
                            instructions = instructionsResult.data,
                            isError = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching details", e)
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}