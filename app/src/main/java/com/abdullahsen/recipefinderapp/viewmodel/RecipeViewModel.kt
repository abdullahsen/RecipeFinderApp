package com.abdullahsen.recipefinderapp.viewmodel

import androidx.lifecycle.*
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.repository.RecipeRepository
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

class RecipeViewModel(private val repository: RecipeRepository):ViewModel() {

    fun insert(recipe:Recipe) = viewModelScope.launch {
        repository.insertRecipeData(recipe)
    }

    val allRecipesList:LiveData<List<Recipe>> = repository.getAllRecipesList.asLiveData()

    fun update(recipe:Recipe) = viewModelScope.launch {
        repository.updateRecipeData(recipe)
    }

    val allFavouriteRecipesList:LiveData<List<Recipe>> = repository.getAllFavouriteRecipesList.asLiveData()

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.deleteRecipeData(recipe)
    }

    fun getFilteredRecipesList(value: String): LiveData<List<Recipe>> =
        repository.filteredListRecipes(value).asLiveData()

}

class RecipeViewModelFactory(private val repository: RecipeRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecipeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow viewmodel class")
    }

}