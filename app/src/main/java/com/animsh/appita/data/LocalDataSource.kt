package com.animsh.appita.data

import com.animsh.appita.data.database.RecipesDao
import com.animsh.appita.data.database.entity.FavoriteEntity
import com.animsh.appita.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by animsh on 3/9/2021.
 */
class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    fun readDatabase(): Flow<List<RecipeEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        recipesDao.insertRecipes(recipeEntity)
    }

    fun readFavRecipe(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavRecipe()
    }

    suspend fun insertFavRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavRecipe(favoriteEntity)
    }

    suspend fun deleteFavRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.deleteFavRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavRecipes() {
        recipesDao.deleteAllFavRecipes()
    }
}