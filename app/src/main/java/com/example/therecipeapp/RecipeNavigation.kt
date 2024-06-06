package com.example.therecipeapp

import android.net.Uri
import androidx.navigation.NavHostController
import com.example.therecipeapp.models.recipes.RecipeModel
import com.google.gson.Gson

object RecipeDestination {
    const val SPLASH = "splash"
    const val GREETING = "greeting"
    const val HOME = "home"
    const val RECIPE = "recipe/{id}/{title}/{image}"
    const val FAVORITES = "favorites"
}

class RecipeNavigationActions(private val navController: NavHostController) {
    fun navigateToGreeting() {
        navController.navigate(RecipeDestination.GREETING) {
            popUpTo(RecipeDestination.SPLASH) {
                saveState = true
            }
        }
    }

    fun navigateToHome() {
        navController.navigate(RecipeDestination.HOME) {
            popUpTo(RecipeDestination.GREETING) {
                inclusive = true
                saveState = true
            }
        }
    }

    fun navigateToRecipe(recipeId: Int, recipeTitle: String, recipeImage: String) {
        navController.navigate("recipe/${recipeId}/${Uri.encode(recipeTitle)}/${Uri.encode(recipeImage)}") {
            popUpTo(RecipeDestination.HOME) {
                saveState = true
            }
        }
    }

    fun navigateToFavorites() {
        navController.navigate(RecipeDestination.FAVORITES) {
            popUpTo(RecipeDestination.FAVORITES) {
                saveState = true
            }
        }
    }
}