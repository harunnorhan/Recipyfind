package com.example.therecipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.therecipeapp.feature.home.HomeScreen
import com.example.therecipeapp.feature.splash.SplashScreen
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
            HomeScreen()
        }

        composable(
            route = RecipeDestination.RECIPE
        ) {

        }

        composable(
            route = RecipeDestination.FAVORITES
        ) {

        }
    }
}