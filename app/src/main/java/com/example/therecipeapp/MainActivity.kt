package com.example.therecipeapp

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    private var isInternetAvailable: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internetConnectionService = InternetConnectionService(this)
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isInternetAvailable = true
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                isInternetAvailable = false
            }
        }
        internetConnectionService.registerNetworkCallback(networkCallback)

        setContent {
            val navController: NavHostController = rememberNavController()
            TheRecipeAppTheme {
                RecipeNavigationGraph(
                    navController = navController,
                    imageLoader = imageLoader,
                    isInternetAvailable = isInternetAvailable
                )
            }
        }

        scheduleRecipeUpdateWorker()
    }
    override fun onResume() {
        super.onResume()
        isInternetAvailable = internetConnectionService.isInternetAvailable()
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
}