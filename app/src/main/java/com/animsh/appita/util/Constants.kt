package com.animsh.appita.util

/**
 * Created by animsh on 2/20/2021.
 */
class Constants {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "[Your Api Key]"

        // API QUERY
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_SORT = "sort"
        const val QUERY_SORT_DIRECTION = "sortDirection"
        const val QUERY_INSTRUCTION_REQUIRED = "instructionsRequired"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_ADD_NUTRITION = "addRecipeNutrition"

        // Room Database
        const val RECIPE_TABLE = "recipes_table"
        const val RECIPE_DATABASE = "recipes_database"
        const val FAVORITE_RECIPE_TABLE = "favorite_recipe_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "appita_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"

    }
}