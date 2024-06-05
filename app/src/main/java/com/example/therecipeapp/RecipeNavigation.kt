package com.example.therecipeapp

import androidx.navigation.NavHostController
import com.example.therecipeapp.models.recipes.RecipeModel

object RecipeDestination {
    const val SPLASH = "splash"
    const val GREETING = "greeting"
    const val HOME = "home"
    const val RECIPE = "recipe/{recipe}"
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

    fun navigateToRecipe(recipeModel: RecipeModel) {
        navController.currentBackStackEntry?.arguments?.putParcelable("recipe", recipeModel)
        navController.navigate(RecipeDestination.RECIPE) {
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