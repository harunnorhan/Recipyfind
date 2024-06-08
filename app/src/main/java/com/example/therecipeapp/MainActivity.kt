package com.example.therecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.example.therecipeapp.ui.theme.TheRecipeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            val navActions = remember(navController) {
                RecipeNavigationActions(navController)
            }
            TheRecipeAppTheme {
                RecipeNavigationGraph(
                    navController = navController,
                    navActions = navActions,
                    imageLoader = imageLoader
                )
            }
        }
    }
}