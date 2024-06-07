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

    fun navigateToRecipe(recipeId: Int) {
        navController.navigate("recipe/${recipeId}") {
            popUpTo(RecipeDestination.HOME) {
                saveState = true
            }
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