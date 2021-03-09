package com.animsh.appita.data

import com.animsh.appita.data.database.RecipeEntity
import com.animsh.appita.data.database.RecipesDao
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
}