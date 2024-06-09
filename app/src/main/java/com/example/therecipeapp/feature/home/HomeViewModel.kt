package com.example.therecipeapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.extensions.recipes.toRecipesModelList
import com.example.therecipeapp.models.recipes.RecipeModel
import com.example.therecipeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val recipes: List<RecipeModel> = emptyList(),
    val selectedMealType: String = mealTypes.first()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val favoritesMap = mutableMapOf<Int, Boolean>()

    init {
        loadFavoriteRecipes()
    }

    private fun loadFavoriteRecipes() {
        viewModelScope.launch {
            repository.getLocalRecipes().collect { favoriteRecipe ->
                favoritesMap.clear()
                favoriteRecipe.forEach {
                    favoritesMap[it.id] = true
                }

                _uiState.update { state ->
                    state.copy(recipes = updateFavoriteStates(state.recipes))
                }
            }
        }
    }

    fun isRecipeFavorite(recipeId: Int): Boolean {
        return favoritesMap[recipeId] ?: false
    }

    private fun updateFavoriteStates(recipes: List<RecipeModel>): List<RecipeModel> {
        return recipes.map { recipe ->
            recipe.copy(isFavorited = favoritesMap[recipe.id] ?: false)
        }
    }

    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            val recipeInDatabase = isRecipeFavorite(recipeId)

            if (recipeInDatabase) {
                repository.deleteRecipe(recipeId)
                _message.emit("Recipe removed from favorites")
            } else {
                val recipe = uiState.value.recipes.find { it.id == recipeId }
                recipe?.let {
                    val localRecipe = LocalRecipes(it.id, it.title ?: "", it.image ?: "")
                    repository.addRecipeToFavorites(localRecipe)
                    _message.emit("Recipe added to favorites")
                }
            }

            favoritesMap[recipeId] = !recipeInDatabase
            _uiState.update { state ->
                state.copy(recipes = updateFavoriteStates(state.recipes))
            }
        }
    }

    fun fetchRecipes(mealType: String) {
        viewModelScope.launch {
            try {
                repository.searchRecipes(type = mealType).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            val recipesModel = result.data?.toRecipesModelList().orEmpty()

                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    recipes = updateFavoriteStates(recipesModel),
                                    selectedMealType = mealType
                                )
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
}