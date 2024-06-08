package com.example.therecipeapp.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val recipes: List<LocalRecipes> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesState())
    val uiState: StateFlow<FavoritesState> = _uiState

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            repository.getLocalRecipes().collect { recipes ->
                _uiState.value = FavoritesState(recipes = recipes)
            }
        }
    }

    fun deleteRecipe(id: Int) {
        viewModelScope.launch {
            repository.deleteRecipe(id)
            _message.emit("Recipe removed from favorites")
        }
    }
}