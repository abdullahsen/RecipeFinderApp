package com.abdullahsen.recipefinderapp.utils

object Constants {
    const val RECIPE_TYPE: String = "RecipeType"
    const val RECIPE_CATEGORY: String = "RecipeCategory"
    const val RECIPE_COOKING_TIME: String = "RecipeCookingTime"

    const val RECIPE_IMAGE_SOURCE_LOCAL = "local"
    const val RECIPE_IMAGE_SOURCE_REMOTE = "remote"

    const val EXTRA_RECIPE_DETAILS = "RecipeDetails"

    const val ALL_ITEMS = "All"
    const val FILTER_SELECTION = "FilterSelection"

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_END_POINT = "recipes/random"
    const val API_KEY = "apiKey"
    const val LIMIT_LICENSE = "limitLicense"
    const val TAGS = "tags"
    const val NUMBER = "number"
    const val API_KEY_VALUE = "Api-Key"
    const val LIMIT_LICENSE_VALUE = true
    const val TAGS_VALUE = "dessert, vegetarian"
    const val NUMBER_VALUE = 1




    fun recipeTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")
        return list
    }

    fun recipeCategories(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("Pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Juices")
        list.add("Sandwich")
        list.add("Tea & Coffee")
        list.add("Wraps")
        list.add("Other")
        return list
    }

    fun recipeCookTime(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }
}