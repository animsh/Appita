package com.animsh.appita.util

/**
 * Created by animsh on 2/20/2021.
 */
class Constants {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "Your Api Key"

        // API QUERY
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Room Database
        const val RECIPE_TABLE = "recipes_table"
        const val RECIPE_DATABASE = "recipes_database"
    }
}