package com.abdullahsen.recipefinderapp.data.remote

import com.abdullahsen.recipefinderapp.data.remote.model.RandomRecipe
import com.abdullahsen.recipefinderapp.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomRecipeAPI {

    @GET(Constants.API_END_POINT)
    fun getRecipes(
        @Query(Constants.API_KEY) apiKey: String,
        @Query(Constants.LIMIT_LICENSE) limitLicense: Boolean,
        @Query(Constants.TAGS) tags: String,
        @Query(Constants.NUMBER) number: Int
    ):Single<RandomRecipe.Recipes>
}