package com.animsh.appita.data

import com.animsh.appita.data.network.FoodRecipesAPi
import com.animsh.appita.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by animsh on 2/20/2021.
 */
class RemoteDataSource @Inject constructor(
    private val foodRecipesAPi: FoodRecipesAPi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesAPi.getRecipes(queries)
    }

    suspend fun searchRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesAPi.searchRecipes(queries)
    }
}