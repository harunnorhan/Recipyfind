package com.example.therecipeapp.feature.recipe

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.local.toLocalRecipe
import com.example.therecipeapp.data.source.network.extensions.informations.toInformationModel
import com.example.therecipeapp.models.informations.InformationModel

import com.example.therecipeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val recipeId: Int = 0,
    val information: InformationModel? = null
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeState())
    val uiState: StateFlow<RecipeState> = _uiState

    fun loadRecipeData(recipeId: Int) {
        Log.d("RecipeViewModel", "Loaded recipe with id: $recipeId")
        _uiState.value = RecipeState(recipeId = recipeId)
        fetchDetails(recipeId = recipeId)
    }

    private fun fetchDetails(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getRecipeInformation(id = recipeId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            val informationModel = result.data?.toInformationModel()

                            _uiState.update { state ->
                                state.copy(isLoading = false, information = informationModel)
                            }
                        }
                        is ApiResult.Loading -> {
                            _uiState.update { state ->
                                state.copy(isLoading = true, isError = false)
                            }
                        }
                        is ApiResult.Error -> {
                            _uiState.update { state ->
                                state.copy(isLoading = false, isError = true)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching details", e)
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun toggleFavorite() { // Favori durumu değiştirme fonksiyonu
        viewModelScope.launch {
            val state = _uiState.value
            val recipeId = state.recipeId
            val information = state.information

            if (recipeId != 0) {
                val isFavorited = repository.isRecipeFavorited(recipeId)

                if (isFavorited) {
                    repository.removeRecipeFromFavorites(recipeId)
                    Log.d("RecipeViewModel", "Recipe removed from favorites: $recipeId")
                } else if (information != null) {
                    val localRecipe = information.toLocalRecipe()
                    repository.addRecipeToFavorites(localRecipe)
                    Log.d("RecipeViewModel", "Recipe added to favorites: $recipeId")
                }
            }
        }
    }
}

