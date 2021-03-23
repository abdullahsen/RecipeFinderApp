package com.abdullahsen.recipefinderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes_table ORDER BY ID")
    fun getAllRecipes(): Flow<List<Recipe>>

}