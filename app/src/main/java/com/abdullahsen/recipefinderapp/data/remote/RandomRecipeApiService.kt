package com.abdullahsen.recipefinderapp.data.remote

import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RandomRecipeApiService {

    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val service = retrofit.create(RandomRecipeAPI::class.java)

    fun getRandomRecipe(): Single<RandomRecipe.Recipes> {
        return service.getRecipes(
            Constants.API_KEY_VALUE,
            Constants.LIMIT_LICENSE_VALUE,
            Constants.TAGS_VALUE,
            Constants.NUMBER_VALUE
        )
    }
}