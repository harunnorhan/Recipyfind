package com.example.therecipeapp.feature.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.local.toLocalRecipe
import com.example.therecipeapp.data.source.network.extensions.informations.toInformationModel
import com.example.therecipeapp.models.informations.InformationModel

import com.example.therecipeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isFavorite: Boolean = false,
    val recipeId: Int = 0,
    val information: InformationModel? = null
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeState())
    val uiState: StateFlow<RecipeState> = _uiState

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    suspend fun loadRecipeData(recipeId: Int) {
        _uiState.update { it.copy(recipeId = recipeId) }

        val isFavorite = repository.isRecipeFavorited(recipeId)
        _uiState.update { it.copy(isFavorite = isFavorite) }

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
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val state = _uiState.value
            val recipeId = state.recipeId
            val information = state.information

            if (recipeId != 0) {
                val isCurrentlyFavorite = state.isFavorite

                if (isCurrentlyFavorite) {
                    repository.removeRecipeFromFavorites(recipeId)
                    _message.emit("Recipe removed from favorites")
                } else if (information != null) {
                    val localRecipe = information.toLocalRecipe()
                    repository.addRecipeToFavorites(localRecipe)
                    _message.emit("Recipe added to favorites")
                }

                _uiState.update { recipeState ->
                    recipeState.copy(isFavorite = !isCurrentlyFavorite)
                }
            }
        }
    }
}

