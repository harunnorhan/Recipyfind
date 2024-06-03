package com.example.therecipeapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
}