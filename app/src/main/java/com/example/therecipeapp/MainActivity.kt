package com.example.therecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import com.example.therecipeapp.ui.theme.TheRecipeAppTheme
import com.example.therecipeapp.workmanager.RecipeCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
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

        scheduleRecipeUpdateWorker()
    }

    private fun scheduleRecipeUpdateWorker() {
        val workRequest = PeriodicWorkRequestBuilder<RecipeCheckWorker>(6, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RecipeCheckWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}