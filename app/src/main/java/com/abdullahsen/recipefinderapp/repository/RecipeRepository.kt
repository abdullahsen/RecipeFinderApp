package com.abdullahsen.recipefinderapp.repository

import androidx.annotation.WorkerThread
import com.abdullahsen.recipefinderapp.data.local.dao.RecipeDao
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao:RecipeDao) {

    @WorkerThread
    suspend fun insertRecipeData(recipe:Recipe){
        recipeDao.insertRecipe(recipe)
    }

    val getAllRecipesList: Flow<List<Recipe>> = recipeDao.getAllRecipes()

}