package com.animsh.appita.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.data.database.entity.RecipeEntity

/**
 * Created by animsh on 3/6/2021.
 */
@Database(
    entities = [RecipeEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipesDao

}