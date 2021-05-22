package com.abdullahsen.recipefinderapp.di.module

import android.content.Context
import androidx.room.Room
import com.abdullahsen.recipefinderapp.data.local.RecipeRoomDatabase
import com.abdullahsen.recipefinderapp.data.local.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context):RecipeRoomDatabase{
        return Room.databaseBuilder(
            appContext,
            RecipeRoomDatabase::class.java,
            "recipe_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(database: RecipeRoomDatabase): RecipeDao {
        return database.recipeDao()
    }
}