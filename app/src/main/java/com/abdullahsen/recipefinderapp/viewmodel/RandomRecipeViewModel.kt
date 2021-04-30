package com.abdullahsen.recipefinderapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdullahsen.recipefinderapp.data.remote.RandomRecipeApiService
import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RandomRecipeViewModel: ViewModel() {

    private val randomRecipeApiService = RandomRecipeApiService()

    private val compositeDisposable = CompositeDisposable()

    val loadRandomRecipe = MutableLiveData<Boolean>()
    val randomRecipeResponse = MutableLiveData<RandomRecipe.Recipes>()
    val randomRecipeLoadingError = MutableLiveData<Boolean>()


    fun getRandomRecipeFromApi(){

        loadRandomRecipe.value = true

        val disposable = randomRecipeApiService.getRandomRecipe()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<RandomRecipe.Recipes>() {
                override fun onSuccess(recipes: RandomRecipe.Recipes?) {
                    loadRandomRecipe.value = false
                    randomRecipeResponse.value = recipes
                    randomRecipeLoadingError.value = false
                }

                override fun onError(e: Throwable?) {
                    loadRandomRecipe.value = false
                    randomRecipeLoadingError.value = true
                }

            })
        compositeDisposable.add(disposable)
    }


}