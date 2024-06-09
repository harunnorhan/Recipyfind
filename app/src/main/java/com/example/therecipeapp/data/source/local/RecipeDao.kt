package com.example.therecipeapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: LocalRecipes)

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: Int): LocalRecipes?

    @Query("SELECT * FROM recipes")
    fun observeRecipes(): Flow<List<LocalRecipes>>

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteRecipeById(id: Int)

    @Query("DELETE FROM recipes")
    suspend fun clearAllRecipes()

    @Query("SELECT id FROM recipes")
    suspend fun getAllRecipeIds(): List<Int>
}