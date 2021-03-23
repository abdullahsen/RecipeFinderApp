package com.abdullahsen.recipefinderapp

import android.app.Application
import com.abdullahsen.recipefinderapp.data.local.RecipeRoomDatabase
import com.abdullahsen.recipefinderapp.repository.RecipeRepository

class RecipeFinderApplication:Application() {

    private val database by lazy {
        RecipeRoomDatabase.getDatabase(this@RecipeFinderApplication)
    }

    val repository by lazy { RecipeRepository(database.recipeDao()) }
}