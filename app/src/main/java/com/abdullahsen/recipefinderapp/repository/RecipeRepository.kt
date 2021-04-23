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

    @WorkerThread
    suspend fun updateRecipeData(recipe:Recipe){
        recipeDao.updateRecipeDetails(recipe)
    }

    val getAllFavouriteRecipesList: Flow<List<Recipe>> = recipeDao.getFavouriteRecipes()

    @WorkerThread
    suspend fun deleteRecipeData(recipe: Recipe){
        recipeDao.deleteRecipe(recipe)
    }

    fun filteredListRecipes(value:String): Flow<List<Recipe>> =
        recipeDao.getFilteredRecipesList(value)

}