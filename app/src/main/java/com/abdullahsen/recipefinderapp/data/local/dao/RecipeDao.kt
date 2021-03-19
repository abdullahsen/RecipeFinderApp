package com.abdullahsen.recipefinderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: Recipe)

}