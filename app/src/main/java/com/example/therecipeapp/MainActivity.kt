package com.example.therecipeapp

import android.app.AlertDialog
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.View
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
import com.example.therecipeapp.service.InternetConnectionService
import com.example.therecipeapp.ui.theme.TheRecipeAppTheme
import com.example.therecipeapp.workmanager.RecipeCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var imageLoader: ImageLoader
    private lateinit var internetConnectionService: InternetConnectionService
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        internetConnectionService = InternetConnectionService(this)

        networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                setNavigationGraph(isInternetAvailable = true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                setNavigationGraph(isInternetAvailable = false)
            }
        }
        internetConnectionService.registerNetworkCallback(networkCallback)

        setNavigationGraph(isInternetAvailable = internetConnectionService.isInternetAvailable())

        scheduleRecipeUpdateWorker()
    }

    override fun onDestroy() {
        super.onDestroy()
        internetConnectionService.unregisterNetworkCallback(networkCallback)
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

    private fun setNavigationGraph(isInternetAvailable: Boolean) {
        setContent {
            val navController: NavHostController = rememberNavController()
            val navActions = remember(navController) { RecipeNavigationActions(navController) }
            TheRecipeAppTheme {
                RecipeNavigationGraph(
                    navController = navController,
                    imageLoader = imageLoader,
                    isInternetAvailable = isInternetAvailable
                )
            }
        }
    }
}