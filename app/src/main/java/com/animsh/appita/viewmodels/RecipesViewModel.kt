package com.animsh.appita.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animsh.appita.data.DataStoreRepository
import com.animsh.appita.util.Constants.Companion.API_KEY
import com.animsh.appita.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.animsh.appita.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.animsh.appita.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.animsh.appita.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.animsh.appita.util.Constants.Companion.QUERY_API_KEY
import com.animsh.appita.util.Constants.Companion.QUERY_DIET
import com.animsh.appita.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.animsh.appita.util.Constants.Companion.QUERY_INSTRUCTION_REQUIRED
import com.animsh.appita.util.Constants.Companion.QUERY_NUMBER
import com.animsh.appita.util.Constants.Companion.QUERY_SORT
import com.animsh.appita.util.Constants.Companion.QUERY_SORT_DIRECTION
import com.animsh.appita.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var mealType = DEFAULT_MEAL_TYPE
    var dietType = DEFAULT_DIET_TYPE

    private val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        viewModelScope.launch {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect {
                mealType = it.selectedMealType
                dietType = it.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_INSTRUCTION_REQUIRED] = "true"
        queries[QUERY_SORT] = "popularity"
        queries[QUERY_SORT_DIRECTION] = "asc"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}