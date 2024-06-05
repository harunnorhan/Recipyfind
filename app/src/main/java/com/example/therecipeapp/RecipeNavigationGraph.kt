package com.example.therecipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.therecipeapp.feature.home.HomeScreen
import com.example.therecipeapp.feature.recipe.RecipeScreen
import com.example.therecipeapp.feature.recipe.RecipeViewModel
import com.example.therecipeapp.feature.splash.SplashScreen
import com.example.therecipeapp.models.recipes.RecipeModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun RecipeNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = RecipeDestination.SPLASH,
    navActions: RecipeNavigationActions = remember(navController) { RecipeNavigationActions(navController) }
) {

    val currentNavigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavigationBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = RecipeDestination.SPLASH
        ) {
            SplashScreen(
                onSplashFinished = {
                    navActions.navigateToHome()
                }
            )
        }

        composable(
            route = RecipeDestination.GREETING
        ) {

        }

        composable(
            route = RecipeDestination.HOME
        ) {
            HomeScreen(
                onRecipeClick = { recipe ->
                    navActions.navigateToRecipe(recipeModel = recipe)
                }
            )
        }

        composable(
            route = RecipeDestination.RECIPE,
            arguments = listOf(
                navArgument("recipe") {
                    type = NavType.ParcelableType(RecipeModel::class.java)
                    defaultValue = null // Varsayılan değeri null olarak ayarlayın
                }
            )
        ) { backStackEntry ->
            val recipe = backStackEntry.arguments?.getParcelable<RecipeModel>("recipe")
            if (recipe != null) {
                RecipeScreen(recipe = recipe)
            } else {
                // Hata durumu, gerekirse işleyin
            }
        }

        composable(
            route = RecipeDestination.FAVORITES
        ) {

        }
    }
}