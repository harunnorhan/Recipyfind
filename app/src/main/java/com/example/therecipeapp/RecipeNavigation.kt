package com.example.therecipeapp

import androidx.navigation.NavHostController

object RecipeDestination {
    const val SPLASH = "splash"
    const val GREETING = "greeting"
    const val HOME = "home"
    const val RECIPE = "recipe/{id}"
    const val FAVORITES = "favorites"
}

class RecipeNavigationActions(private val navController: NavHostController) {
    fun navigateToGreeting() {
        navController.navigate(RecipeDestination.GREETING)
    }

    fun navigateToHome() {
        navController.navigate(RecipeDestination.HOME)
    }

    fun navigateToRecipe(recipeId: Int) {
        navController.navigate("recipe/${recipeId}") {
            launchSingleTop = true
        }
    }

    fun navigateToFavorites() {
        navController.navigate(RecipeDestination.FAVORITES) {
            popUpTo(RecipeDestination.HOME) {
                saveState = true
            }
        }
    }
}