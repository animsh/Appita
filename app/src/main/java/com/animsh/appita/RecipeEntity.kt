package com.animsh.appita

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.animsh.appita.models.FoodRecipe
import com.animsh.appita.util.Constants.Companion.RECIPE_TABLE

/**
 * Created by animsh on 3/6/2021.
 */
@Entity(tableName = RECIPE_TABLE)
class RecipeEntity(
    val foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}