package com.example.therecipeapp.feature.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.network.extensions.recipes.toRecipesModelList
import com.example.therecipeapp.models.recipes.RecipeModel
import com.example.therecipeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val recipes: List<RecipeModel> = emptyList(),
    val selectedMealType: String = "main course"
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    fun fetchRecipes(mealType: String) {
        viewModelScope.launch {
            try {
                repository.searchRecipes(type = mealType).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            val recipesModel = result.data?.toRecipesModelList().orEmpty()

                            _uiState.update { state ->
                                state.copy(isLoading = false, recipes = recipesModel, selectedMealType = mealType)
                            }
                        }
                        is ApiResult.Loading -> {
                            _uiState.update { state ->
                                state.copy(isLoading = true, isError = false, selectedMealType = mealType)
                            }
                        }
                        is ApiResult.Error -> {
                            _uiState.update { state ->
                                state.copy(isLoading = false, isError = true, selectedMealType = mealType)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(isLoading = false, isError = true, selectedMealType = mealType)
                }
            }
        }
    }

    fun saveSelectedRecipe(recipe: RecipeModel) {
        savedStateHandle["selectedRecipe"] = recipe
        Log.v("SavedStateHandle", "Saved recipe with id: ${recipe.id}")
    }
}