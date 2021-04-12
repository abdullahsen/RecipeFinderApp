package com.abdullahsen.recipefinderapp.data.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "recipes_table")
@Parcelize
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id:Long = 0,
    @ColumnInfo val image:String,
    @ColumnInfo(name = "image_source") val imageSource:String,
    @ColumnInfo val title:String,
    @ColumnInfo val type:String,
    @ColumnInfo val category:String,
    @ColumnInfo val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo(name = "cooking_direction") val cookingDirection: String,
    @ColumnInfo(name = "favourite_recipe") val favouriteRecipe: Boolean
) : Parcelable