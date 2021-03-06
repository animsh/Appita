package com.animsh.appita.data.database

import androidx.room.TypeConverter
import com.animsh.appita.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by animsh on 3/6/2021.
 */
class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun recipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }
}