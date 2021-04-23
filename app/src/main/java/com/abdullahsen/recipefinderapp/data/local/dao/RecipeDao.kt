package com.abdullahsen.recipefinderapp.data.local.dao

import androidx.room.*
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes_table ORDER BY ID")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Update
    suspend fun updateRecipeDetails(recipe: Recipe)

    @Query("SELECT * FROM recipes_table WHERE favourite_recipe = 1")
    fun getFavouriteRecipes(): Flow<List<Recipe>>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes_table WHERE type=:filterType")
    fun getFilteredRecipesList(filterType:String): Flow<List<Recipe>>

}