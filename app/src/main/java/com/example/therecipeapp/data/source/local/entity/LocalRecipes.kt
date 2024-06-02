package com.example.therecipeapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.therecipeapp.data.source.network.response.recipes.AnalyzedInstruction
import com.example.therecipeapp.data.source.network.response.recipes.ExtendedIngredient

@Entity(
    tableName = "recipes"
)
data class LocalRecipes(
    @PrimaryKey val id: Int,
    var title: String,
    var image: String,
    var servings: Int,
    var readyInMinutes: Int,
    //var extendedIngredients: List<ExtendedIngredient?>?,
    //var analyzedInstructions: List<AnalyzedInstruction?>?
)