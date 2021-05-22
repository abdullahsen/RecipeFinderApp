package com.abdullahsen.recipefinderapp.viewmodel

import androidx.lifecycle.*
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe
import com.abdullahsen.recipefinderapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository):ViewModel() {

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
