package com.abdullahsen.recipefinderapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdullahsen.recipefinderapp.data.local.dao.RecipeDao
import com.abdullahsen.recipefinderapp.data.local.entities.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeRoomDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

}