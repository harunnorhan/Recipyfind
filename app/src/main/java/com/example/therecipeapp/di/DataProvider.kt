package com.example.therecipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.therecipeapp.data.RecipeRepository
import com.example.therecipeapp.data.RecipeRepositoryImpl
import com.example.therecipeapp.data.source.local.MIGRATION_1_3
import com.example.therecipeapp.data.source.local.RecipeDao
import com.example.therecipeapp.data.source.local.RecipeDatabase
import com.example.therecipeapp.data.source.network.NetworkDataSource
import com.example.therecipeapp.data.source.network.RecipeApiService
import com.example.therecipeapp.data.source.network.RecipeNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabasesModule {
    @Provides
    @Singleton
    fun provideRecipesDao(database: RecipeDatabase): RecipeDao {
        return database.recipesDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(context.applicationContext, RecipeDatabase::class.java, "recipe_database.db")
            .addMigrations(MIGRATION_1_3)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DataProviderModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(recipeApiService: RecipeApiService): NetworkDataSource {
        return RecipeNetworkDataSource(recipeApiService)
    }

    @Provides
    @Singleton
    fun provideMortyApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/recipes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}