package com.abdullahsen.recipefinderapp.repository

import androidx.annotation.WorkerThread
import com.abdullahsen.recipefinderapp.data.local.dao.RecipeDao
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.data.remote.RandomRecipeAPI
import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeService: RandomRecipeAPI
) {

    @WorkerThread
    suspend fun insertRecipeData(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    val getAllRecipesList: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    @WorkerThread
    suspend fun updateRecipeData(recipe: Recipe) {
        recipeDao.updateRecipeDetails(recipe)
    }

    val getAllFavouriteRecipesList: Flow<List<Recipe>> = recipeDao.getFavouriteRecipes()

    @WorkerThread
    suspend fun deleteRecipeData(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    fun filteredListRecipes(value: String): Flow<List<Recipe>> =
        recipeDao.getFilteredRecipesList(value)


    @WorkerThread
    suspend fun getRandomRecipe(): RandomRecipe.Recipes = recipeService.getRecipes(
        Constants.API_KEY_VALUE,
        Constants.LIMIT_LICENSE_VALUE,
        Constants.TAGS_VALUE,
        Constants.NUMBER_VALUE
    )


}