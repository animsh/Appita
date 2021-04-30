package com.animsh.appita.data.database

import androidx.room.*
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by animsh on 3/6/2021.
 */
@Dao
interface RecipesDao {

    // for offline recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipeEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipeEntity>>

    // for favorite recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavRecipe(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_recipe_table ORDER BY id ASC")
    fun readFavRecipe(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavRecipe(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_recipe_table")
    suspend fun deleteAllFavRecipes()
}