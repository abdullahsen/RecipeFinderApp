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