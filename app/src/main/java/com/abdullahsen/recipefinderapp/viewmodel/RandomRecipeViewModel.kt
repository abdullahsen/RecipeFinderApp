package com.abdullahsen.recipefinderapp.viewmodel

import androidx.lifecycle.*
import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.repository.RecipeRepository
import com.abdullahsen.recipefinderapp.utils.RecipeApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class RandomRecipeViewModel @Inject constructor(private val repository: RecipeRepository): ViewModel() {

    //val randomRecipeResponse = MutableLiveData<RandomRecipe.Recipes>()

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<RecipeApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<RecipeApiStatus>
        get() = _status


    // Internally, we use a MutableLiveData, because we will be updating the Recipe
    // with new values
    private val _recipe = MutableLiveData<RandomRecipe.Recipes>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val recipe: LiveData<RandomRecipe.Recipes>
        get() = _recipe


    fun getRandomRecipeFromApi(){
        viewModelScope.launch {
            _status.value = RecipeApiStatus.LOADING
            try {
                _recipe.value = repository.getRandomRecipe()
            }catch (e: Exception){
                e.printStackTrace()
                _status.value = RecipeApiStatus.ERROR
            }
            _status.value = RecipeApiStatus.DONE
        }
    }

}
