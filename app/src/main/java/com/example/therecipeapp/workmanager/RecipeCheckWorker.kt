package com.example.therecipeapp.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.therecipeapp.R
import com.example.therecipeapp.data.source.local.RecipeDao
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.data.source.network.NetworkDataSource
import com.example.therecipeapp.data.source.network.extensions.informations.toInformationModel
import com.example.therecipeapp.data.source.network.extensions.informations.toIngredientModel
import com.example.therecipeapp.models.informations.InformationModel
import com.example.therecipeapp.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext

class RecipeCheckWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val localDataSource: RecipeDao,
    private val networkDataSource: NetworkDataSource
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val recipeIds = getLocalRecipeIds()

            recipeIds.forEach { recipeId ->
                val localRecipe = getLocalRecipe(recipeId)
                val remoteRecipe = fetchRemoteRecipe(recipeId)

                if (localRecipe != null && (localRecipe.id != remoteRecipe.id || localRecipe.title != remoteRecipe.title || localRecipe.image != remoteRecipe.image)) {
                    sendNotification()
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun getLocalRecipeIds(): List<Int> {
        return localDataSource.getAllRecipeIds()
    }

    private suspend fun getLocalRecipe(recipeId: Int): LocalRecipes? {
        return localDataSource.getRecipeById(recipeId)
    }

    private suspend fun fetchRemoteRecipe(recipeId: Int): InformationModel {
        return when (val response = networkDataSource.getRecipeInformation(recipeId).single()) {
            is ApiResult.Success -> {
                response.data?.toInformationModel() ?: InformationModel(
                    id = response.data?.id,
                    image = response.data?.image,
                    title = response.data?.title,
                    instructions = response.data?.instructions,
                    ingredients = response.data?.extendedIngredients?.mapNotNull { it?.toIngredientModel() },
                    servings = response.data?.servings,
                    sourceUrl = response.data?.sourceUrl,
                    readyInMinutes = response.data?.readyInMinutes
                )
            }
            else -> InformationModel(
                id = null,
                image = null,
                title = null,
                instructions = null,
                ingredients = null,
                servings = null,
                sourceUrl = null,
                readyInMinutes = null
            )
        }
    }

    private fun sendNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("recipe_updates", "Recipe Updates", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Notifications for Recipe Updates"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "recipe_updates")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Recipe Update")
            .setContentText("Your saved recipes have been updated!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}