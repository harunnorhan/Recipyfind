package com.example.therecipeapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.therecipeapp.data.source.local.entity.LocalRecipes

@Database(entities = [LocalRecipes::class], version = 3, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipeDao
}