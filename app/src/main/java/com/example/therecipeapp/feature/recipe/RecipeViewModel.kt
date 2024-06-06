package com.example.therecipeapp.feature.recipe

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.network.extensions.ingredients.toIngredientModelList
import com.example.therecipeapp.models.ingredients.IngredientModel
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
    val ingredients: List<IngredientModel>? = null,
    //val instructions: List<InstructionsResponse>? = null
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeState())
    val uiState: StateFlow<RecipeState> = _uiState

    fun loadRecipeData(recipeId: Int, recipeTitle: String, recipeImage: String) {
        Log.d("RecipeViewModel", "Loaded recipe with id: $recipeId")
        Log.d("RecipeViewModel", "Loaded recipe with title: $recipeTitle")
        Log.d("RecipeViewModel", "Loaded recipe with image: $recipeImage")
        _uiState.value = RecipeState(
            recipe = RecipeModel(id = recipeId, title = recipeTitle, image = recipeImage)
        )
        fetchDetails(recipeId = recipeId)
    }

    private fun fetchDetails(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val ingredientsResult = repository.getIngredientsById(recipeId).first()
                if (ingredientsResult is ApiResult.Success) {
                    val ingredientsModel = ingredientsResult.data?.toIngredientModelList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ingredients = ingredientsModel,
                            isError = false
                        )
                    }
                } else {
                    Log.d("RecipeViewModel", "Error api")
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching details", e)
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}