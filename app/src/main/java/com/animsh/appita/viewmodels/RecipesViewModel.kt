package com.animsh.appita.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(context: Context): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { values ->
                mealType = values.selectedMealType
                dietType = values.selectedDietType
            }
        }

        val sharedPreference =
            context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        mealType = sharedPreference.getString("meal", DEFAULT_MEAL_TYPE).toString()
        dietType = sharedPreference.getString("diet", DEFAULT_DIET_TYPE).toString()
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

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(getApplication(), "Internet Connection Is Back!!", Toast.LENGTH_SHORT)
                .show()
        }
    }

}